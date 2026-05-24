package com.heineken.auth.service.impl;

import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.response.AtRiskUserResponse;
import com.heineken.auth.model.dto.response.CourseStatsResponse;
import com.heineken.auth.model.dto.response.InstructorProfileResponse;
import com.heineken.auth.model.dto.response.RecentActivityResponse;
import com.heineken.auth.model.dto.response.WeeklyActivityResponse;
import com.heineken.auth.model.entity.Course;
import com.heineken.auth.model.entity.Enrollment;
import com.heineken.auth.model.entity.UserAccess;
import com.heineken.auth.repository.CourseRepository;
import com.heineken.auth.repository.EnrollmentRepository;
import com.heineken.auth.repository.UserAccessRepository;
import com.heineken.auth.repository.UserRepository;
import com.heineken.auth.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserAccessRepository userAccessRepository;

    @Override
    public InstructorProfileResponse getInstructorProfile(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + email));

        long totalCourses = courseRepository.countByStatus("ACTIVE");
        long totalStudents = enrollmentRepository.count();

        return new InstructorProfileResponse(user.getName(), user.getEmail(), totalCourses, totalStudents);
    }

    @Override
    public List<CourseStatsResponse> getCourseStats() {
        List<Course> activeCourses = courseRepository.findByStatus("ACTIVE");
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        return activeCourses.stream()
                .map(course -> {
                    long totalEnrollments = enrollmentRepository.findByCourseId(course.getId()).size();
                    long completedCount = enrollmentRepository.countCompletedByCourseId(course.getId());
                    long weeklyAccesses = userAccessRepository.countByCourseIdAndAccessedAtAfter(course.getId(), sevenDaysAgo);

                    double completionRate = totalEnrollments > 0
                            ? completedCount * 100.0 / totalEnrollments
                            : 0.0;

                    return new CourseStatsResponse(course.getId(), course.getTitle(), weeklyAccesses, completionRate);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AtRiskUserResponse> getAtRiskUsers() {
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();

        // Agrupa matrículas por usuário e filtra onde a média de progresso é < 50
        Map<com.heineken.auth.model.entity.User, List<Enrollment>> byUser = allEnrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getUser));

        List<AtRiskUserResponse> atRiskUsers = new ArrayList<>();

        for (var entry : byUser.entrySet()) {
            var user = entry.getKey();
            var enrollments = entry.getValue();

            double avgProgress = enrollments.stream()
                    .mapToInt(Enrollment::getProgress)
                    .average()
                    .orElse(0.0);

            if (avgProgress < 50) {
                // Inclui uma entrada por matrícula com progresso baixo
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getProgress() < 50) {
                        atRiskUsers.add(new AtRiskUserResponse(
                                user.getName(),
                                user.getEmail(),
                                enrollment.getCourse().getTitle(),
                                enrollment.getProgress(),
                                enrollment.getLastAccessedAt()
                        ));
                    }
                }
            }
        }

        return atRiskUsers;
    }

    @Override
    public List<WeeklyActivityResponse> getWeeklyActivity() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        List<WeeklyActivityResponse> weeklyActivity = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            LocalDateTime start = day.atStartOfDay();
            LocalDateTime end = day.plusDays(1).atStartOfDay();

            List<UserAccess> accesses = userAccessRepository.findByAccessedAtBetween(start, end);

            long accessCount = accesses.size();
            long completionCount = accesses.stream()
                    .filter(a -> Boolean.TRUE.equals(a.getCompletedLesson()))
                    .count();

            String dayName = day.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));

            weeklyActivity.add(new WeeklyActivityResponse(dayName, accessCount, completionCount));
        }

        return weeklyActivity;
    }

    @Override
    public List<RecentActivityResponse> getRecentActivity() {
        List<UserAccess> recentAccesses = userAccessRepository.findTop10ByOrderByAccessedAtDesc();

        return recentAccesses.stream()
                .map(access -> {
                    Integer progress = enrollmentRepository
                            .findByUserIdAndCourseId(access.getUser().getId(), access.getCourse().getId())
                            .map(Enrollment::getProgress)
                            .orElse(0);

                    return new RecentActivityResponse(
                            access.getUser().getName(),
                            access.getCourse().getTitle(),
                            progress,
                            access.getAccessedAt()
                    );
                })
                .collect(Collectors.toList());
    }
}

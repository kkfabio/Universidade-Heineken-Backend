package com.heineken.auth.service.impl;

import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.request.CreateCourseRequest;
import com.heineken.auth.model.dto.request.CreateModuleRequest;
import com.heineken.auth.model.dto.request.UpdateCourseRequest;
import com.heineken.auth.model.dto.request.UpdateModuleRequest;
import com.heineken.auth.model.dto.response.CourseResponse;
import com.heineken.auth.model.dto.response.ModuleResponse;
import com.heineken.auth.model.entity.Course;
import com.heineken.auth.model.entity.Module;
import com.heineken.auth.repository.CourseRepository;
import com.heineken.auth.repository.LessonRepository;
import com.heineken.auth.repository.ModuleRepository;
import com.heineken.auth.service.AdminCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCourseServiceImpl implements AdminCourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Override
    public List<CourseResponse> getCourses(String status, String category) {
        List<Course> courses;

        if (status != null && category != null) {
            courses = courseRepository.findByStatusAndCategory(status, category);
        } else if (status != null) {
            courses = courseRepository.findByStatus(status);
        } else if (category != null) {
            courses = courseRepository.findByCategory(category);
        } else {
            courses = courseRepository.findAll();
        }

        return courses.stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse createCourse(CreateCourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .level(request.getLevel())
                .durationHours(request.getDurationHours())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .createdAt(LocalDateTime.now())
                .build();

        return toCourseResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse updateCourse(UUID id, UpdateCourseRequest request) {
        Course course = findCourseById(id);

        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getCategory() != null) {
            course.setCategory(request.getCategory());
        }
        if (request.getLevel() != null) {
            course.setLevel(request.getLevel());
        }
        if (request.getDurationHours() != null) {
            course.setDurationHours(request.getDurationHours());
        }
        if (request.getStatus() != null) {
            course.setStatus(request.getStatus());
        }

        return toCourseResponse(courseRepository.save(course));
    }

    @Override
    public ModuleResponse createModule(UUID courseId, CreateModuleRequest request) {
        Course course = findCourseById(courseId);

        Module module = Module.builder()
                .course(course)
                .title(request.getTitle())
                .durationMinutes(request.getDurationMinutes())
                .orderIndex(request.getOrderIndex())
                .build();

        return toModuleResponse(moduleRepository.save(module));
    }

    @Override
    public ModuleResponse updateModule(UUID courseId, UUID moduleId, UpdateModuleRequest request) {
        findCourseById(courseId);
        Module module = findModuleById(moduleId);

        if (request.getTitle() != null) {
            module.setTitle(request.getTitle());
        }
        if (request.getDurationMinutes() != null) {
            module.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getOrderIndex() != null) {
            module.setOrderIndex(request.getOrderIndex());
        }

        return toModuleResponse(moduleRepository.save(module));
    }

    @Override
    public void deleteModule(UUID courseId, UUID moduleId) {
        findCourseById(courseId);
        Module module = findModuleById(moduleId);
        moduleRepository.delete(module);
    }

    // --- helpers ---

    private Course findCourseById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + id));
    }

    private Module findModuleById(UUID id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado: " + id));
    }

    private ModuleResponse toModuleResponse(Module module) {
        long lessonCount = lessonRepository.countByModuleId(module.getId());
        return new ModuleResponse(
                module.getId(),
                module.getTitle(),
                module.getDurationMinutes(),
                module.getOrderIndex(),
                (int) lessonCount
        );
    }

    private CourseResponse toCourseResponse(Course course) {
        List<ModuleResponse> modules = moduleRepository.findByCourseId(course.getId()).stream()
                .map(this::toModuleResponse)
                .collect(Collectors.toList());

        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getCategory(),
                course.getLevel(),
                course.getDurationHours(),
                course.getStatus(),
                modules
        );
    }
}

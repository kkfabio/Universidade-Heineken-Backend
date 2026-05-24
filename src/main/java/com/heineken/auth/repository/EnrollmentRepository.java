package com.heineken.auth.repository;

import com.heineken.auth.model.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByUserId(UUID userId);

    List<Enrollment> findByCourseId(UUID courseId);

    Optional<Enrollment> findByUserIdAndCourseId(UUID userId, UUID courseId);

    @Query("SELECT AVG(e.progress) FROM Enrollment e WHERE e.user.id = :userId")
    Double avgProgressByUserId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.progress = 100")
    long countCompletedByCourseId(@Param("courseId") UUID courseId);
}

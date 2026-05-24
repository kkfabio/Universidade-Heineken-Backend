package com.heineken.auth.repository;

import com.heineken.auth.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByStatus(String status);

    List<Course> findByStatusAndCategory(String status, String category);

    List<Course> findByCategory(String category);

    long countByStatus(String status);
}

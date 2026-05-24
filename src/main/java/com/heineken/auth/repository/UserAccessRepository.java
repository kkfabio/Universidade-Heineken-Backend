package com.heineken.auth.repository;

import com.heineken.auth.model.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserAccessRepository extends JpaRepository<UserAccess, UUID> {

    long countByCourseIdAndAccessedAtAfter(UUID courseId, LocalDateTime since);

    List<UserAccess> findTop10ByOrderByAccessedAtDesc();

    List<UserAccess> findByAccessedAtBetween(LocalDateTime start, LocalDateTime end);
}

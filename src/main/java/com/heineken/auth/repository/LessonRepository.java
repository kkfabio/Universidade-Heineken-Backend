package com.heineken.auth.repository;

import com.heineken.auth.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByModuleId(UUID moduleId);

    void deleteAllByModuleId(UUID moduleId);

    long countByModuleId(UUID moduleId);
}

package com.heineken.auth.repository;

import com.heineken.auth.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {

    List<Module> findByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}

package com.heineken.auth.repository;

import com.heineken.auth.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT u FROM User u
            WHERE (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
                                   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:active IS NULL OR u.active = :active)
              AND (:role IS NULL OR u.role = :role)
            """)
    Page<User> findByFilters(
            @Param("search") String search,
            @Param("active") Boolean active,
            @Param("role") String role,
            Pageable pageable
    );
}
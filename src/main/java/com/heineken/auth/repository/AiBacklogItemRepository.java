package com.heineken.auth.repository;

import com.heineken.auth.model.entity.AiBacklogItem;
import com.heineken.auth.model.entity.AiBacklogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiBacklogItemRepository extends JpaRepository<AiBacklogItem, UUID> {

    Page<AiBacklogItem> findByStatus(AiBacklogStatus status, Pageable pageable);
}

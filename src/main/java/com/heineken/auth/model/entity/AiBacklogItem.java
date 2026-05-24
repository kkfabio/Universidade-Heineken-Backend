package com.heineken.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_backlog_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiBacklogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiBacklogStatus status;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

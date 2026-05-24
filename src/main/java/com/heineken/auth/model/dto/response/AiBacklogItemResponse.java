package com.heineken.auth.model.dto.response;

import com.heineken.auth.model.entity.AiBacklogStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AiBacklogItemResponse {
    private UUID id;
    private String title;
    private String description;
    private AiBacklogStatus status;
    private String priority;
    private LocalDateTime createdAt;
    private String category;
}

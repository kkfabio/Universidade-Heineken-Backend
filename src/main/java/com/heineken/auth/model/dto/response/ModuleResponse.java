package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ModuleResponse {
    private UUID id;
    private String title;
    private Integer durationMinutes;
    private Integer orderIndex;
    private Integer lessonCount;
}

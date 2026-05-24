package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseResponse {
    private UUID id;
    private String title;
    private String category;
    private String level;
    private Integer durationHours;
    private String status;
    private List<ModuleResponse> modules;
}

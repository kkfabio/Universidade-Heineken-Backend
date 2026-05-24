package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseStatsResponse {
    private UUID courseId;
    private String title;
    private Long weeklyAccesses;
    private Double completionRate;
}

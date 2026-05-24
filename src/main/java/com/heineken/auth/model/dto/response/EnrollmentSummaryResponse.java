package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EnrollmentSummaryResponse {
    private UUID courseId;
    private String courseTitle;
    private Integer progress;
    private LocalDateTime lastAccessedAt;
}

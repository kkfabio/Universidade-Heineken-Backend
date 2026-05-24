package com.heineken.auth.model.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDetailResponse extends UserSummaryResponse {

    private LocalDateTime createdAt;
    private List<EnrollmentSummaryResponse> enrollments;

    public UserDetailResponse(UUID id, String name, String email, String department,
                               String role, Double avgProgress, LocalDateTime lastAccessDate,
                               Boolean active, LocalDateTime createdAt,
                               List<EnrollmentSummaryResponse> enrollments) {
        super(id, name, email, department, role, avgProgress, lastAccessDate, active);
        this.createdAt = createdAt;
        this.enrollments = enrollments;
    }
}

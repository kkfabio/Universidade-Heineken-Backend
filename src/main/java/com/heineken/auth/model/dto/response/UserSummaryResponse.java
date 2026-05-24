package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    private UUID id;
    private String name;
    private String email;
    private String department;
    private String role;
    private Double avgProgress;
    private LocalDateTime lastAccessDate;
    private Boolean active;
}

package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AtRiskUserResponse {
    private String userName;
    private String email;
    private String courseName;
    private Integer progress;
    private LocalDateTime lastAccessDate;
}

package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecentActivityResponse {
    private String userName;
    private String courseName;
    private Integer progress;
    private LocalDateTime lastAccessAt;
}

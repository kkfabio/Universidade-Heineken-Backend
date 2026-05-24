package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyActivityResponse {
    private String dayName;
    private Long accessCount;
    private Long completionCount;
}

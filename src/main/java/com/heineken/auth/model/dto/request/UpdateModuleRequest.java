package com.heineken.auth.model.dto.request;

import lombok.Data;

@Data
public class UpdateModuleRequest {

    private String title;
    private Integer durationMinutes;
    private Integer orderIndex;
}

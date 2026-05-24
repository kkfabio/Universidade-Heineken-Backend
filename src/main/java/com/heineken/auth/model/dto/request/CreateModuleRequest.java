package com.heineken.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateModuleRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private Integer durationMinutes;

    private Integer orderIndex;
}

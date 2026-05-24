package com.heineken.auth.model.dto.request;

import com.heineken.auth.model.entity.AiBacklogStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAiBacklogStatusRequest {

    @NotNull(message = "Status is required")
    private AiBacklogStatus status;
}

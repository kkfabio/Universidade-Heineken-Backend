package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.CreateAiBacklogRequest;
import com.heineken.auth.model.dto.request.UpdateAiBacklogStatusRequest;
import com.heineken.auth.model.dto.response.AiBacklogItemResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.entity.AiBacklogStatus;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AiBacklogService {

    PagedResponse<AiBacklogItemResponse> getItems(AiBacklogStatus status, Pageable pageable);

    AiBacklogItemResponse createItem(CreateAiBacklogRequest request);

    AiBacklogItemResponse updateItemStatus(UUID id, UpdateAiBacklogStatusRequest request);
}

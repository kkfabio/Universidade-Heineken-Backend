package com.heineken.auth.service.impl;

import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.request.CreateAiBacklogRequest;
import com.heineken.auth.model.dto.request.UpdateAiBacklogStatusRequest;
import com.heineken.auth.model.dto.response.AiBacklogItemResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.entity.AiBacklogItem;
import com.heineken.auth.model.entity.AiBacklogStatus;
import com.heineken.auth.repository.AiBacklogItemRepository;
import com.heineken.auth.service.AiBacklogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiBacklogServiceImpl implements AiBacklogService {

    private final AiBacklogItemRepository aiBacklogItemRepository;

    @Override
    public PagedResponse<AiBacklogItemResponse> getItems(AiBacklogStatus status, Pageable pageable) {
        Page<AiBacklogItem> page = status != null
                ? aiBacklogItemRepository.findByStatus(status, pageable)
                : aiBacklogItemRepository.findAll(pageable);

        List<AiBacklogItemResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public AiBacklogItemResponse createItem(CreateAiBacklogRequest request) {
        AiBacklogItem item = AiBacklogItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .category(request.getCategory())
                .status(AiBacklogStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(aiBacklogItemRepository.save(item));
    }

    @Override
    public AiBacklogItemResponse updateItemStatus(UUID id, UpdateAiBacklogStatusRequest request) {
        AiBacklogItem item = aiBacklogItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de backlog não encontrado: " + id));

        item.setStatus(request.getStatus());

        return toResponse(aiBacklogItemRepository.save(item));
    }

    private AiBacklogItemResponse toResponse(AiBacklogItem item) {
        return new AiBacklogItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getStatus(),
                item.getPriority(),
                item.getCreatedAt(),
                item.getCategory()
        );
    }
}

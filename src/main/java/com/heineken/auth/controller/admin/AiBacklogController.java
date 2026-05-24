package com.heineken.auth.controller.admin;

import com.heineken.auth.model.dto.request.CreateAiBacklogRequest;
import com.heineken.auth.model.dto.request.UpdateAiBacklogStatusRequest;
import com.heineken.auth.model.dto.response.AiBacklogItemResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.entity.AiBacklogStatus;
import com.heineken.auth.service.AiBacklogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/ai-backlog")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AiBacklogController {

    private final AiBacklogService aiBacklogService;

    @GetMapping
    public ResponseEntity<PagedResponse<AiBacklogItemResponse>> getItems(
            @RequestParam(required = false) AiBacklogStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(aiBacklogService.getItems(status, pageable));
    }

    @PostMapping
    public ResponseEntity<AiBacklogItemResponse> createItem(
            @Valid @RequestBody CreateAiBacklogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aiBacklogService.createItem(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AiBacklogItemResponse> updateItemStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAiBacklogStatusRequest request) {
        return ResponseEntity.ok(aiBacklogService.updateItemStatus(id, request));
    }
}

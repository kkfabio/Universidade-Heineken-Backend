package com.heineken.auth.service.impl;

import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.request.CreatePostRequest;
import com.heineken.auth.model.dto.request.UpdatePostRequest;
import com.heineken.auth.model.dto.response.CompanyPostResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.entity.CompanyPost;
import com.heineken.auth.repository.CompanyPostRepository;
import com.heineken.auth.service.CompanyPostService;
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
public class CompanyPostServiceImpl implements CompanyPostService {

    private final CompanyPostRepository companyPostRepository;

    @Override
    public PagedResponse<CompanyPostResponse> getPosts(Pageable pageable) {
        Page<CompanyPost> page = companyPostRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<CompanyPostResponse> content = page.getContent().stream()
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
    public CompanyPostResponse createPost(CreatePostRequest request) {
        CompanyPost post = CompanyPost.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(companyPostRepository.save(post));
    }

    @Override
    public CompanyPostResponse updatePost(UUID id, UpdatePostRequest request) {
        CompanyPost post = companyPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado: " + id));

        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setUpdatedAt(LocalDateTime.now());

        return toResponse(companyPostRepository.save(post));
    }

    @Override
    public void deletePost(UUID id) {
        CompanyPost post = companyPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post não encontrado: " + id));

        companyPostRepository.delete(post);
    }

    private CompanyPostResponse toResponse(CompanyPost post) {
        return new CompanyPostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}

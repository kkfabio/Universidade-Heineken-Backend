package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.CreatePostRequest;
import com.heineken.auth.model.dto.request.UpdatePostRequest;
import com.heineken.auth.model.dto.response.CompanyPostResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyPostService {

    PagedResponse<CompanyPostResponse> getPosts(Pageable pageable);

    CompanyPostResponse createPost(CreatePostRequest request);

    CompanyPostResponse updatePost(UUID id, UpdatePostRequest request);

    void deletePost(UUID id);
}

package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.CreateUserRequest;
import com.heineken.auth.model.dto.request.UpdateUserRequest;
import com.heineken.auth.model.dto.request.UpdateUserStatusRequest;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.dto.response.UserDetailResponse;
import com.heineken.auth.model.dto.response.UserSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminUserService {

    PagedResponse<UserSummaryResponse> getUsers(Pageable pageable, String search, String status, String role);

    UserDetailResponse getUserById(UUID id);

    UserDetailResponse createUser(CreateUserRequest request);

    UserDetailResponse updateUser(UUID id, UpdateUserRequest request);

    UserDetailResponse updateUserStatus(UUID id, UpdateUserStatusRequest request);
}

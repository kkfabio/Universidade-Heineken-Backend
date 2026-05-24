package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.LoginRequest;
import com.heineken.auth.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}


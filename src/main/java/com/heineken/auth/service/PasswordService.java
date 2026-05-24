package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.ChangePasswordRequest;
import com.heineken.auth.model.dto.request.ForgotPasswordRequest;
import com.heineken.auth.model.dto.response.ForgotPasswordResponse;

public interface PasswordService {
    ForgotPasswordResponse resetPassword(ForgotPasswordRequest request);
    void changePassword(ChangePasswordRequest request);
    void restorePasswordIfExpired(String email);
}


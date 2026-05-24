package com.heineken.auth.controller;

import com.heineken.auth.model.dto.request.ChangePasswordRequest;
import com.heineken.auth.model.dto.request.ForgotPasswordRequest;
import com.heineken.auth.model.dto.response.ForgotPasswordResponse;
import com.heineken.auth.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PasswordController {

    private final PasswordService passwordService;

    // POST /api/password/forgot
    @PostMapping("/forgot")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = passwordService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/password/change
    @PostMapping("/change")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        passwordService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
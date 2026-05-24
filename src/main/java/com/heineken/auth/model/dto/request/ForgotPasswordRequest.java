package com.heineken.auth.model.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String cpf;
}
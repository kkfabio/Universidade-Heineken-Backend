package com.heineken.auth.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String cpf;
}
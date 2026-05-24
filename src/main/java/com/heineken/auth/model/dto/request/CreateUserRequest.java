package com.heineken.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "CPF is required")
    private String cpf;

    @NotBlank(message = "Role is required")
    private String role;

    private String department;
}

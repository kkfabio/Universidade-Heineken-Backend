package com.heineken.auth.model.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;
    private String email;
    private String department;
    private String role;
    private Boolean active;
}

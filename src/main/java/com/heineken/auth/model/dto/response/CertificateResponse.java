package com.heineken.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CertificateResponse {
    private UUID id;
    private String userName;
    private String courseTitle;
    private LocalDateTime issuedAt;
    private String certificateCode;
}

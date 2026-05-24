package com.heineken.auth.controller.admin;

import com.heineken.auth.model.dto.request.IssueCertificateRequest;
import com.heineken.auth.model.dto.response.CertificateResponse;
import com.heineken.auth.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/certificates")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<List<CertificateResponse>> getCertificates(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID courseId) {
        return ResponseEntity.ok(certificateService.getCertificates(userId, courseId));
    }

    @PostMapping
    public ResponseEntity<CertificateResponse> issueCertificate(
            @Valid @RequestBody IssueCertificateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(certificateService.issueCertificate(request));
    }
}

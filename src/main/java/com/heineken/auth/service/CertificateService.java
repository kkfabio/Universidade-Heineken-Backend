package com.heineken.auth.service;

import com.heineken.auth.model.dto.request.IssueCertificateRequest;
import com.heineken.auth.model.dto.response.CertificateResponse;

import java.util.List;
import java.util.UUID;

public interface CertificateService {

    List<CertificateResponse> getCertificates(UUID userId, UUID courseId);

    CertificateResponse issueCertificate(IssueCertificateRequest request);
}

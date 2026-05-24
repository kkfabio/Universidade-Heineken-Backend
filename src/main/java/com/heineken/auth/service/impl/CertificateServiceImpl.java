package com.heineken.auth.service.impl;

import com.heineken.auth.exception.DuplicateResourceException;
import com.heineken.auth.exception.InsufficientProgressException;
import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.request.IssueCertificateRequest;
import com.heineken.auth.model.dto.response.CertificateResponse;
import com.heineken.auth.model.entity.Certificate;
import com.heineken.auth.model.entity.Enrollment;
import com.heineken.auth.repository.CertificateRepository;
import com.heineken.auth.repository.EnrollmentRepository;
import com.heineken.auth.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<CertificateResponse> getCertificates(UUID userId, UUID courseId) {
        List<Certificate> certificates;

        if (userId != null && courseId != null) {
            certificates = certificateRepository.findByUserIdAndCourseId(userId, courseId)
                    .map(List::of)
                    .orElse(new ArrayList<>());
        } else if (userId != null) {
            certificates = certificateRepository.findByUserId(userId);
        } else if (courseId != null) {
            certificates = certificateRepository.findByCourseId(courseId);
        } else {
            certificates = certificateRepository.findAll();
        }

        return certificates.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CertificateResponse issueCertificate(IssueCertificateRequest request) {
        Enrollment enrollment = enrollmentRepository
                .findByUserIdAndCourseId(request.getUserId(), request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Matrícula não encontrada para o usuário e curso informados"));

        if (enrollment.getProgress() < 100) {
            throw new InsufficientProgressException(
                    "Progresso insuficiente para emitir certificado. Progresso atual: " + enrollment.getProgress() + "%");
        }

        if (certificateRepository.existsByUserIdAndCourseId(request.getUserId(), request.getCourseId())) {
            throw new DuplicateResourceException("Certificado já emitido para este usuário e curso");
        }

        Certificate certificate = Certificate.builder()
                .user(enrollment.getUser())
                .course(enrollment.getCourse())
                .certificateCode(UUID.randomUUID().toString())
                .issuedAt(LocalDateTime.now())
                .build();

        return toResponse(certificateRepository.save(certificate));
    }

    private CertificateResponse toResponse(Certificate certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getUser().getName(),
                certificate.getCourse().getTitle(),
                certificate.getIssuedAt(),
                certificate.getCertificateCode()
        );
    }
}

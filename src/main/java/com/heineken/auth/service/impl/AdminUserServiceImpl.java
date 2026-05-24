package com.heineken.auth.service.impl;

import com.heineken.auth.exception.DuplicateResourceException;
import com.heineken.auth.exception.ResourceNotFoundException;
import com.heineken.auth.model.dto.request.CreateUserRequest;
import com.heineken.auth.model.dto.request.UpdateUserRequest;
import com.heineken.auth.model.dto.request.UpdateUserStatusRequest;
import com.heineken.auth.model.dto.response.EnrollmentSummaryResponse;
import com.heineken.auth.model.dto.response.PagedResponse;
import com.heineken.auth.model.dto.response.UserDetailResponse;
import com.heineken.auth.model.dto.response.UserSummaryResponse;
import com.heineken.auth.model.entity.Enrollment;
import com.heineken.auth.model.entity.User;
import com.heineken.auth.repository.EnrollmentRepository;
import com.heineken.auth.repository.UserRepository;
import com.heineken.auth.service.AdminUserService;
import com.heineken.auth.util.CpfValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PagedResponse<UserSummaryResponse> getUsers(Pageable pageable, String search, String status, String role) {
        Boolean active = parseActiveStatus(status);

        Page<User> page = userRepository.findByFilters(search, active, role, pageable);

        List<UserSummaryResponse> content = page.getContent().stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public UserDetailResponse getUserById(UUID id) {
        User user = findUserById(id);
        return toDetailResponse(user);
    }

    @Override
    public UserDetailResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email já cadastrado: " + request.getEmail());
        }

        String normalizedCpf = CpfValidator.normalizeCpf(request.getCpf());
        String defaultPassword = buildDefaultPassword(normalizedCpf);

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .cpf(normalizedCpf)
                .password(passwordEncoder.encode(defaultPassword))
                .role(request.getRole())
                .department(request.getDepartment())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return toDetailResponse(userRepository.save(user));
    }

    @Override
    public UserDetailResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = findUserById(id);

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getDepartment() != null) {
            user.setDepartment(request.getDepartment());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        return toDetailResponse(userRepository.save(user));
    }

    @Override
    public UserDetailResponse updateUserStatus(UUID id, UpdateUserStatusRequest request) {
        User user = findUserById(id);
        user.setActive(request.getActive());
        return toDetailResponse(userRepository.save(user));
    }

    // --- helpers ---

    private User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    /**
     * Gera a senha padrão: Heineken@ + 3 últimos dígitos do CPF normalizado.
     * Ex: CPF 12345678901 → senha Heineken@901
     */
    private String buildDefaultPassword(String normalizedCpf) {
        String lastThreeDigits = normalizedCpf.substring(normalizedCpf.length() - 3);
        return "Heineken@" + lastThreeDigits;
    }

    private Boolean parseActiveStatus(String status) {
        if (status == null) return null;
        return switch (status.toLowerCase()) {
            case "active" -> true;
            case "inactive" -> false;
            default -> null;
        };
    }

    private UserSummaryResponse toSummaryResponse(User user) {
        Double avgProgress = enrollmentRepository.avgProgressByUserId(user.getId());

        LocalDateTime lastAccessDate = enrollmentRepository.findByUserId(user.getId()).stream()
                .map(Enrollment::getLastAccessedAt)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new UserSummaryResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDepartment(),
                user.getRole(),
                avgProgress,
                lastAccessDate,
                user.getActive()
        );
    }

    private UserDetailResponse toDetailResponse(User user) {
        Double avgProgress = enrollmentRepository.avgProgressByUserId(user.getId());

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(user.getId());

        LocalDateTime lastAccessDate = enrollments.stream()
                .map(Enrollment::getLastAccessedAt)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        List<EnrollmentSummaryResponse> enrollmentSummaries = enrollments.stream()
                .map(e -> new EnrollmentSummaryResponse(
                        e.getCourse().getId(),
                        e.getCourse().getTitle(),
                        e.getProgress(),
                        e.getLastAccessedAt()
                ))
                .collect(Collectors.toList());

        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getDepartment(),
                user.getRole(),
                avgProgress,
                lastAccessDate,
                user.getActive(),
                user.getCreatedAt(),
                enrollmentSummaries
        );
    }
}

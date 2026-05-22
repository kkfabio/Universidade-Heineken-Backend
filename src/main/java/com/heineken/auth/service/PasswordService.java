package com.heineken.auth.service;

import com.heineken.auth.dto.ChangePasswordRequest;
import com.heineken.auth.dto.ForgotPasswordRequest;
import com.heineken.auth.dto.ForgotPasswordResponse;
import com.heineken.auth.model.User;
import com.heineken.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordResponse resetPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        User user = userOpt.get();

        if (!user.getCpf().equals(request.getCpf())) {
            throw new RuntimeException("CPF incorreto");
        }

        // Guarda a senha original antes de substituir
        user.setOriginalPassword(user.getPassword());

        // Senha temporária = 6 primeiros dígitos do CPF
        String temporaryPassword = request.getCpf().replaceAll("[^0-9]", "").substring(0, 6);
        user.setPassword(passwordEncoder.encode(temporaryPassword));

        // Expira em 5 minutos
        user.setTempPasswordExpiresAt(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        return new ForgotPasswordResponse("Senha resetada com sucesso!", temporaryPassword);
    }

    public void changePassword(ChangePasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        // Criptografa a nova senha e limpa os campos temporários
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOriginalPassword(null);
        user.setTempPasswordExpiresAt(null);

        userRepository.save(user);
    }

    public void restorePasswordIfExpired(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        if (user.getTempPasswordExpiresAt() != null
                && LocalDateTime.now().isAfter(user.getTempPasswordExpiresAt())
                && user.getOriginalPassword() != null) {

            user.setPassword(user.getOriginalPassword());
            user.setOriginalPassword(null);
            user.setTempPasswordExpiresAt(null);

            userRepository.save(user);
        }
    }
}
package com.heineken.auth.service.impl;

import com.heineken.auth.exception.InvalidCredentialsException;
import com.heineken.auth.model.dto.request.LoginRequest;
import com.heineken.auth.model.dto.response.AuthResponse;
import com.heineken.auth.model.entity.User;
import com.heineken.auth.repository.UserRepository;
import com.heineken.auth.service.AuthService;
import com.heineken.auth.service.JwtService;
import com.heineken.auth.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordService passwordService;

    @Override
    public AuthResponse login(LoginRequest request) {
        // Verifica se a senha temporária expirou antes de autenticar
        passwordService.restorePasswordIfExpired(request.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        // Mensagem genérica para evitar enumeration attack
        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("Email ou senha inválidos");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}


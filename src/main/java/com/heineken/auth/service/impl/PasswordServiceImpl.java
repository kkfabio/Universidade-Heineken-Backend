package com.heineken.auth.service.impl;

import com.heineken.auth.exception.InvalidCredentialsException;
import com.heineken.auth.exception.InvalidPasswordException;
import com.heineken.auth.exception.UserNotFoundException;
import com.heineken.auth.model.dto.request.ChangePasswordRequest;
import com.heineken.auth.model.dto.request.ForgotPasswordRequest;
import com.heineken.auth.model.dto.response.ForgotPasswordResponse;
import com.heineken.auth.model.entity.User;
import com.heineken.auth.repository.UserRepository;
import com.heineken.auth.service.PasswordService;
import com.heineken.auth.util.CpfValidator;
import com.heineken.auth.util.PasswordValidator;
import com.heineken.auth.util.SecurePasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Reseta a senha do usuário após validação de email e CPF.
     * Gera uma senha segura e aleatória que expira em 5 minutos.
     *
     * @param request Contém email e CPF do usuário
     * @return Mensagem de confirmação (sem expor a senha)
     * @throws UserNotFoundException Se usuário não encontrado
     * @throws InvalidCredentialsException Se CPF não corresponder
     */
    @Override
    public ForgotPasswordResponse resetPassword(ForgotPasswordRequest request) {
        // Valida formato do CPF
        if (!CpfValidator.isValidFormat(request.getCpf())) {
            throw new InvalidCredentialsException("Email ou CPF inválido. Tente novamente.");
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        // Mensagem genérica para evitar enumeration attack
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Email ou CPF inválido. Tente novamente.");
        }

        User user = userOpt.get();
        String normalizedCpf = CpfValidator.normalizeCpf(request.getCpf());
        String storedCpf = CpfValidator.normalizeCpf(user.getCpf());

        // Compara CPFs normalizados
        if (!storedCpf.equals(normalizedCpf)) {
            throw new InvalidCredentialsException("Email ou CPF inválido. Tente novamente.");
        }

        // Guarda a senha original antes de substituir
        user.setOriginalPassword(user.getPassword());

        // Gera senha temporária segura e aleatória
        String temporaryPassword = SecurePasswordGenerator.generateSecurePassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));

        // Expira em 5 minutos
        user.setTempPasswordExpiresAt(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        // NÃO retorna a senha temporária no response (segurança)
        return new ForgotPasswordResponse("Senha temporária foi enviada para o email registrado.", null);
    }

    /**
     * Altera a senha do usuário após validação da senha atual.
     * Valida se a nova senha atende aos requisitos de segurança.
     *
     * @param request Email, senha atual e nova senha
     * @throws UserNotFoundException Se usuário não encontrado
     * @throws InvalidCredentialsException Se senha atual incorreta
     * @throws InvalidPasswordException Se nova senha não atende requisitos
     */
    @Override
    public void changePassword(ChangePasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Usuário não encontrado");
        }

        User user = userOpt.get();

        // Valida a senha atual
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Senha atual incorreta");
        }

        // Valida força da nova senha
        if (!PasswordValidator.isValid(request.getNewPassword())) {
            throw new InvalidPasswordException(PasswordValidator.getValidationErrorMessage());
        }

        // Criptografa a nova senha e limpa os campos temporários
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOriginalPassword(null);
        user.setTempPasswordExpiresAt(null);

        userRepository.save(user);
    }

    /**
     * Restaura a senha original do usuário se a senha temporária expirou.
     * Chamado automaticamente antes da autenticação.
     *
     * @param email Email do usuário
     */
    @Override
    public void restorePasswordIfExpired(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        // Valida se há senha temporária ativa que expirou
        if (user.getTempPasswordExpiresAt() != null
                && LocalDateTime.now().isAfter(user.getTempPasswordExpiresAt())
                && user.getOriginalPassword() != null) {

            // Restaura a senha original e limpa os campos temporários
            user.setPassword(user.getOriginalPassword());
            user.setOriginalPassword(null);
            user.setTempPasswordExpiresAt(null);

            userRepository.save(user);
        }
    }
}


package com.heineken.auth.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecurePasswordGeneratorTest {

    @Test
    void testGenerateSecurePasswordLength() {
        String password = SecurePasswordGenerator.generateSecurePassword();
        assertEquals(16, password.length());
    }

    @Test
    void testGenerateSecurePasswordUniqueness() {
        Set<String> passwords = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            passwords.add(SecurePasswordGenerator.generateSecurePassword());
        }
        // Todos os 100 valores gerados devem ser únicos (altíssima probabilidade)
        assertTrue(passwords.size() > 95);
    }

    @Test
    void testGenerateSecurePasswordHasUppercase() {
        String password = SecurePasswordGenerator.generateSecurePassword();
        assertTrue(password.matches(".*[A-Z].*"));
    }

    @Test
    void testGenerateSecurePasswordHasLowercase() {
        String password = SecurePasswordGenerator.generateSecurePassword();
        assertTrue(password.matches(".*[a-z].*"));
    }

    @Test
    void testGenerateSecurePasswordHasDigit() {
        String password = SecurePasswordGenerator.generateSecurePassword();
        assertTrue(password.matches(".*[0-9].*"));
    }

    @Test
    void testGenerateSecurePasswordHasSpecialChar() {
        String password = SecurePasswordGenerator.generateSecurePassword();
        assertTrue(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?/`~].*"));
    }
}


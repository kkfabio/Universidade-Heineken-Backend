package com.heineken.auth.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void testValidPassword() {
        assertTrue(PasswordValidator.isValid("ValidPass@123"));
        assertTrue(PasswordValidator.isValid("MyPassword!2024"));
    }

    @Test
    void testPasswordTooShort() {
        assertFalse(PasswordValidator.isValid("Pass@1")); // 6 caracteres
    }

    @Test
    void testPasswordMissingUppercase() {
        assertFalse(PasswordValidator.isValid("password@123"));
    }

    @Test
    void testPasswordMissingLowercase() {
        assertFalse(PasswordValidator.isValid("PASSWORD@123"));
    }

    @Test
    void testPasswordMissingDigit() {
        assertFalse(PasswordValidator.isValid("Password@abc"));
    }

    @Test
    void testPasswordMissingSpecialChar() {
        assertFalse(PasswordValidator.isValid("Password123"));
    }

    @Test
    void testValidationErrorMessage() {
        String message = PasswordValidator.getValidationErrorMessage();
        assertNotNull(message);
        assertTrue(message.contains("8 caracteres"));
        assertTrue(message.contains("maiúscula"));
    }
}


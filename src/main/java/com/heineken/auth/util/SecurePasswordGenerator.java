package com.heineken.auth.util;

import java.security.SecureRandom;

public class SecurePasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}';:\"\\|,.<>?/`~";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;
    private static final int PASSWORD_LENGTH = 16;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera uma senha segura com 16 caracteres contendo:
     * - Letras maiúsculas
     * - Letras minúsculas
     * - Números
     * - Caracteres especiais
     */
    public static String generateSecurePassword() {
        StringBuilder password = new StringBuilder();

        // Garante pelo menos um de cada tipo
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Preenche o resto randomicamente
        for (int i = password.length(); i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Embaralha a senha
        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}


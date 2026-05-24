package com.heineken.auth.util;

public class CpfValidator {

    public static String normalizeCpf(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    public static boolean isValidFormat(String cpf) {
        String normalized = normalizeCpf(cpf);
        return normalized != null && normalized.length() == 11 && isNumeric(normalized);
    }

    private static boolean isNumeric(String str) {
        return str.matches("[0-9]+");
    }
}


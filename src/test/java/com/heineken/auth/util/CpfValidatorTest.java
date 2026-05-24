package com.heineken.auth.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    @Test
    void testValidateCpfWithFormatting() {
        assertTrue(CpfValidator.isValidFormat("123.456.789-00"));
        assertTrue(CpfValidator.isValidFormat("12345678900"));
    }

    @Test
    void testValidateCpfInvalidFormat() {
        assertFalse(CpfValidator.isValidFormat("123.456"));
        assertFalse(CpfValidator.isValidFormat("abcdefghijk"));
    }

    @Test
    void testNormalizeCpf() {
        assertEquals("12345678900", CpfValidator.normalizeCpf("123.456.789-00"));
        assertEquals("12345678900", CpfValidator.normalizeCpf("12345678900"));
    }

    @Test
    void testNormalizeCpfNull() {
        assertNull(CpfValidator.normalizeCpf(null));
    }

    @Test
    void testCpfComparisonWithDifferentFormats() {
        String cpf1 = CpfValidator.normalizeCpf("123.456.789-00");
        String cpf2 = CpfValidator.normalizeCpf("12345678900");
        assertEquals(cpf1, cpf2);
    }
}


package com.heineken.auth.service;

public interface JwtService {
    String generateToken(String email);
    String extractEmail(String token);
    boolean isTokenValid(String token);
}


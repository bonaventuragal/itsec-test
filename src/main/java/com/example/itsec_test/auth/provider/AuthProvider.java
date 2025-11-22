package com.example.itsec_test.auth.provider;

public interface AuthProvider {
    String encodePassword(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}

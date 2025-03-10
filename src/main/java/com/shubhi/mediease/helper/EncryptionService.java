package com.shubhi.mediease.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptionService {

    private final PasswordEncoder passwordEncoder;

    @Value("${password.hashing.enabled}")  // Read value from application.properties
    private boolean passwordHashingEnabled;

    /**
     * Encodes the password if hashing is enabled, otherwise returns the raw password.
     */
    public String encode(String password) {
        if (passwordHashingEnabled) {
            return passwordEncoder.encode(password); // Hash password
        }
        return password; // Store as plain text
    }

    /**
     * Validates the entered password against the stored password.
     * Handles both hashed and plain text password cases.
     */
    public boolean validates(String password, String encodedPassword) {
        if (passwordHashingEnabled) {
            return passwordEncoder.matches(password, encodedPassword); // Compare hashed passwords
        }
        return password.equals(encodedPassword); // Compare plain passwords
    }
}

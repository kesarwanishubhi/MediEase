package com.shubhi.mediease.service;

import ch.qos.logback.core.boolex.Matcher;
import com.shubhi.mediease.dto.LoginRequest;
import com.shubhi.mediease.entity.Role;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.helper.EncryptionService;
import com.shubhi.mediease.helper.JwtHelper;
import com.shubhi.mediease.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final JwtHelper jwtHelper;
    private final UsersRepo usersRepo;
    private  final EncryptionService encryptionService;

    public ResponseEntity<?> loginChecking(LoginRequest request) {
        // Find user by email
        Users user = usersRepo.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password using EncryptionService
        if (!encryptionService.validates(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Extract user role
        Role role = user.getRole(); // Assuming Role enum has getPermissions()

        // Generate JWT Token
        String token = jwtHelper.generateToken(user.getEmail(), role);

        // Return response with JWT token
        return ResponseEntity.ok(Map.of(
                "message", "Login Successful",
                "token", token
        ));
    }


    public boolean adminChecking(String token) {
        // Extract role from the token
        String role = jwtHelper.extractRole(token);

        // Check if the role is ADMIN
        return Role.ADMIN.name().equals(role);
    }
}


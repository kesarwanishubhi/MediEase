package com.shubhi.mediease.controller;

import com.shubhi.mediease.dto.Admindetails;
import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.dto.LoginRequest;
import com.shubhi.mediease.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shubhi.mediease.service.LoginService;
import jakarta.servlet.http.HttpSession;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final LoginService LoginService;
    private final DoctorService doctorService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        // Call loginChecking method from LoginService
        ResponseEntity<?> response = LoginService.loginChecking(request);

        // Return response directly
        return response;
    }
    @PostMapping("/registerDoctor")
    public ResponseEntity<?> registerDoctor(@RequestHeader("Authorization") String token, @RequestBody DoctorResponse response) {
        // Remove "Bearer " from the token string
        token = token.substring(7);

        // Check if the user is an admin
        boolean isAdmin = LoginService.adminChecking(token);

        if (isAdmin) {
            return ResponseEntity.ok(doctorService.create(response));
        } else {
            return ResponseEntity.status(403).body("You are not an admin");
        }
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(Admindetails admindetails) {

        return null;
    }




}


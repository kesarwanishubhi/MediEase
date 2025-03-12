package com.shubhi.mediease.controller;

import com.shubhi.mediease.dto.*;
import com.shubhi.mediease.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shubhi.mediease.service.LoginService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class DoctorController {
    private final LoginService LoginService;
    private final DoctorService doctorService;


    @PostMapping("/complete-profile")
    public ResponseEntity<DoctorProfileResponse> completeProfile(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody DoctorProfileRequest request) {

        // Remove 'Bearer ' from token
        String cleanedToken = token.substring(7);

        DoctorProfileResponse response = doctorService.completeDoctorProfile(cleanedToken, request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/update-profile")
    public ResponseEntity<String> updateDoctorProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody DoctorUpdate doctorUpdateDTO) {

        String t = token.substring(7); // Remove "Bearer " prefix
        doctorService.updateDoctorProfile(t, doctorUpdateDTO);
        return ResponseEntity.ok("Doctor profile updated successfully");
    }

}
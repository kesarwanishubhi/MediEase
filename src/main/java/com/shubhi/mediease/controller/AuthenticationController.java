package com.shubhi.mediease.controller;

import com.shubhi.mediease.dto.*;
import com.shubhi.mediease.entity.Hospitals;
import com.shubhi.mediease.helper.JwtHelper;
import com.shubhi.mediease.service.DoctorService;
import com.shubhi.mediease.service.HospitalService;
import com.shubhi.mediease.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shubhi.mediease.service.LoginService;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final LoginService LoginService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final JwtHelper jwtService;
    private final HospitalService hospitalService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        // Call loginChecking method from LoginService
        ResponseEntity<?> response = LoginService.loginChecking(request);

        // Return response directly
        return response;
    }
    @PostMapping("/registerDoctor")
    public ResponseEntity<?> registerDoctor(@RequestHeader("Authorization") String token, @RequestBody DoctorReq response) {
        // Remove "Bearer " from the token string

        //log.info("Received Doctor Registration Request: {}", response);
        System.out.println("Received request to register doctor: " + response);
        token = token.substring(7);

        // Check if the user is an admin
        boolean isAdmin = LoginService.adminChecking(token);

        if (isAdmin) {

            return ResponseEntity.ok(doctorService.create(response));
        } else {
            return ResponseEntity.status(403).body("You are not an admin");
        }
    }
    @PostMapping("/registerPatient")
    public ResponseEntity<?> registerPatient(@RequestBody RegisterPatient dto) {
        DoctorResponse response = patientService.registerPatient(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(Admindetails admindetails) {

        return null;

    }
    @PostMapping("/createhospital")
    public ResponseEntity<String> createHospital(
            @Valid @RequestBody HospitalCreate hospitalDTO,
            @RequestHeader("Authorization") String token) {

        // 🔹 Extract JWT token from Authorization header
        token = token.substring(7);

        // 🔹 Extract role and permissions from token
        Map<String, Object> rolePermissions = jwtService.extractRoleAndPermissions(token);
        String role = (String) rolePermissions.get("role");

        // 🔹 Authorization check: Only admins can create a hospital
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Access denied! Only admins can create hospitals.");
        }

        // 🔹 Create hospital
        Hospitals hospital = hospitalService.createHospital(hospitalDTO);
        return ResponseEntity.ok("Hospital '" + hospital.getName() + "' created successfully!");
    }





}


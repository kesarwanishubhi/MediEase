package com.shubhi.mediease.controller;



import com.shubhi.mediease.dto.FileUploadRequest;
import com.shubhi.mediease.dto.OtpVerificationRequest;
import com.shubhi.mediease.helper.JwtHelper;
import com.shubhi.mediease.service.ConsentService;
import com.shubhi.mediease.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    private final PatientService patientFileService;
    private final ConsentService consentService;
    private final JwtHelper jwtHelper;

    @PostMapping("/uploadfile")
    public ResponseEntity<String> uploadFile(
            @RequestParam("files") MultipartFile[] files,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("No files uploaded.");
        }

        try {
            // Extract email from the token
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String email = jwtHelper.extractEmail(token);
            System.out.println("Extracted email: " + email);

            // Call service layer to handle file upload
            for (MultipartFile file : files) {
                patientFileService.uploadFile(email, file);
            }

            return ResponseEntity.ok("Files uploaded successfully.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload files: " + e.getMessage());
        }
    }



    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of("Missing or invalid Authorization header."));
        }

        try {
            // Extract email from JWT token
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            String email = jwtHelper.extractEmail(token);

            // Retrieve file names
            List<String> fileNames = patientFileService.getAllFileNames(email);
            return ResponseEntity.ok(fileNames.isEmpty() ? List.of("No files found.") : fileNames);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of("Error retrieving files: " + e.getMessage()));
        }
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, @RequestHeader("Authorization") String token) {
        try {
            // Extract email from JWT token
            String email = jwtHelper.extractEmail(token);

            byte[] fileData = patientFileService.getFile(email, fileName);
            return ResponseEntity.ok(fileData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName, @RequestHeader("Authorization") String token) {
        try {
            String email = jwtHelper.extractEmail(token); // Extract email from JWT
            String response = patientFileService.deleteFile(email, fileName);
            return response.contains("successfully") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete file: " + e.getMessage());
        }
    }
    @PostMapping("/checker")
    public ResponseEntity<String> otpChecker(@RequestHeader("Authorization") String token,@RequestBody OtpVerificationRequest req)
    {

            if(consentService.verifyOtp(req)) return ResponseEntity.ok("verified successfully.");
            else return ResponseEntity.badRequest().body("OTP verification failed.");

    }



}





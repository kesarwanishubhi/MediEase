package com.shubhi.mediease.controller;



import com.shubhi.mediease.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientFileService;

    @PostMapping("/uploadfile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, Principal principal) {
        try {
            String response = patientFileService.storeFile(principal.getName(), file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/listfiles")
    public ResponseEntity<List<String>> listFiles(Principal principal) {
        String folderPath = "D:/PatientDocs/" + principal.getName();
        List<String> fileNames = patientFileService.getAllFileNames(folderPath);

        return ResponseEntity.ok(fileNames.isEmpty() ? List.of("No files found.") : fileNames);
    }

    @GetMapping("/downloadfile/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, Principal principal) {
        try {
            byte[] fileData = patientFileService.getFile(principal.getName(), fileName);
            return ResponseEntity.ok(fileData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/deletefile/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName, Principal principal) {
        String response = patientFileService.deleteFile(principal.getName(), fileName);
        return response.contains("successfully") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}

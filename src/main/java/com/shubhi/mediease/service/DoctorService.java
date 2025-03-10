package com.shubhi.mediease.service;

import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.entity.Role;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.helper.EncryptionService;
import com.shubhi.mediease.mapper.DoctorMapper;
import com.shubhi.mediease.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.shubhi.mediease.helper.EncryptionService;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UsersRepo userRepo;
    private final DoctorMapper doctorMapper;
    private final EncryptionService encryptionService; // Inject EncryptionService

    public DoctorResponse create(DoctorResponse response) {
        // Check if the email is already registered
        Optional<Users> existingUser = userRepo.findByEmail(response.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Doctor with email " + response.email() + " is already registered.");
        }

        // Generate a random password
        String rawPassword = generateRandomPassword(8);

        // Hash the password only if AUTH_ENABLED=true
        String storedPassword = encryptionService.encode(rawPassword);

        // Map DTO to Entity and set role
        Users doctor = doctorMapper.toEntity(
                new DoctorResponse(response.username(), response.email(), response.phone()),
                storedPassword
        );
        doctor.setRole(Role.DOCTOR);

        // Save doctor in database
        userRepo.save(doctor);

        // Convert back to DTO (return raw password for admin to share)
        return doctorMapper.toDto(doctor, rawPassword);
    }

    // Generates a random password
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
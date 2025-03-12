package com.shubhi.mediease.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shubhi.mediease.dto.*;
import com.shubhi.mediease.entity.Doctors;
import com.shubhi.mediease.entity.Role;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.helper.EncryptionService;
import com.shubhi.mediease.helper.JwtHelper;
import com.shubhi.mediease.helper.ResourceNotFoundException;
import com.shubhi.mediease.mapper.DoctorMapper;
import com.shubhi.mediease.repo.Doctorsrepo;
import com.shubhi.mediease.repo.UsersRepo;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final UsersRepo userRepo;
    private final Doctorsrepo doctorRepository;
    private final DoctorMapper doctorMapper;
    private final JwtHelper jwtHelper;
    private final EncryptionService encryptionService; // Inject EncryptionService
    private final ObjectMapper objectMapper;

    public DoctorResponse create(DoctorReq response) {

        // Validate email is not empty
        if (response.email() == null || response.email().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        // Check if the email is already registered
        Optional<Users> existingUser = userRepo.findByEmail(response.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Doctor with email " + response.email() + " is already registered.");
        }

        // Generate a random password
        String rawPassword = generateRandomPassword(8);

        // Hash the password
        String storedPassword = encryptionService.encode(rawPassword);

        // Create doctor entity using builder
        Users doctor = Users.builder()
                .username(response.username())
                .email(response.email().trim()) // Trim to avoid accidental spaces
                .phone(response.phone())
                .password(storedPassword) // Store encoded password
                .role(Role.DOCTOR)
                .isActive(true)
                .failedLoginAttempts(0)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Save doctor in database
        userRepo.save(doctor);

        // Convert back to DTO (return raw password for admin to share)
        return doctorMapper.toDto(doctor, rawPassword);
    }


    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
    public DoctorProfileResponse completeDoctorProfile(String token, DoctorProfileRequest request) {
        // Extract email from token
        String email = jwtHelper.extractEmail(token);

        // Fetch User based on email
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Edge Case: Profile already exists
        if (doctorRepository.findByUser(user).isPresent()) {
            throw new ValidationException("Doctor profile is already completed.");
        }

        // Edge Case: Experience and Fee should be valid
        if (request.experienceYears() <= 0) {
            throw new ValidationException("Experience years must be greater than 0.");
        }
        if (request.consultationFee() < 0) {
            throw new ValidationException("Consultation fee cannot be negative.");
        }

        // Edge Case: Validate Schedule
        validateSchedule(request.schedule());

        // Convert Schedule to JSON
        String scheduleJson = convertScheduleToJson(request.schedule());

        // Create Doctor Entity using Builder
        Doctors doctor = Doctors.builder()
                .user(user)
                .specialization(request.specialization())
                .experienceYears(request.experienceYears())
                .qualification(request.qualification())
                .consultationFee(request.consultationFee())
                .hospitalAddress(request.hospitalAddress())
                .schedule(scheduleJson)
                .isApproved(false) // Admin approval required
                .build();

        doctorRepository.save(doctor);

        // Return Response DTO
        return doctorMapper.toProfile(doctor);
    }

    /**
     * Validates if the schedule format is correct.
     */


    public void validateSchedule(Map<String, List<String>> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            throw new ValidationException("Schedule cannot be empty.");
        }
        try {
            objectMapper.writeValueAsString(schedule); // Ensures valid JSON format
        } catch (JsonProcessingException e) {
            throw new ValidationException("Invalid schedule format: " + e.getMessage());
        }
    }


    /**
     * Converts a schedule Map<String, List<String>> to a JSON String.
     */
    private String convertScheduleToJson(Map<String, List<String>> schedule) {
        try {
            return objectMapper.writeValueAsString(schedule);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting schedule to JSON format.");
        }
    }
    public void updateDoctorProfile(String token, DoctorUpdate doctorUpdateDTO) {
        // ✅ Extract email from token securely
        String email = jwtHelper.extractEmail(token);

        // ✅ Validate user existence
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // ✅ Fetch doctor profile
        Doctors doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));

        // ✅ Extract role and permissions from token
        Map<String, Object> rolePermissions = jwtHelper.extractRoleAndPermissions(token);
        String role = (String) rolePermissions.get("role");
        List<String> permissions = (List<String>) rolePermissions.get("permissions");

        // ✅ Validate Role & Update Permission
        if (!"DOCTOR".equals(role) || permissions == null || !permissions.contains("UPDATE")) {
            throw new SecurityException("You do not have permission to update the profile.");
        }

        // ✅ Ensure Doctor Cannot Change Email
        if (!doctor.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("You are not allowed to change your email ID.");
        }

        // ✅ Update Fields Only If Provided and Valid
        boolean isUpdated = false; // Track if an update is made

        if (doctorUpdateDTO.getSpecialization() != null && !doctorUpdateDTO.getSpecialization().isBlank()) {
            doctor.setSpecialization(doctorUpdateDTO.getSpecialization());
            isUpdated = true;
        }

        if (doctorUpdateDTO.getExperienceYears() != null && doctorUpdateDTO.getExperienceYears() > 0) {
            doctor.setExperienceYears(doctorUpdateDTO.getExperienceYears());
            isUpdated = true;
        }

        if (doctorUpdateDTO.getQualification() != null && !doctorUpdateDTO.getQualification().isBlank()) {
            doctor.setQualification(doctorUpdateDTO.getQualification());
            isUpdated = true;
        }

        if (doctorUpdateDTO.getConsultationFee() != null && doctorUpdateDTO.getConsultationFee() >= 0) {
            doctor.setConsultationFee(doctorUpdateDTO.getConsultationFee());
            isUpdated = true;
        }

        if (doctorUpdateDTO.getHospitalAddress() != null && !doctorUpdateDTO.getHospitalAddress().isBlank()) {
            doctor.setHospitalAddress(doctorUpdateDTO.getHospitalAddress());
            isUpdated = true;
        }

        if (doctorUpdateDTO.getSchedule() != null && !doctorUpdateDTO.getSchedule().isBlank()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // Validate if the input is a proper JSON
                JsonNode jsonNode = objectMapper.readTree(doctorUpdateDTO.getSchedule());
                doctor.setSchedule(jsonNode.toString());  // Store properly formatted JSON
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Invalid schedule format. Must be valid JSON.");
            }
        }

        // ✅ Handle Password Update
        if (doctorUpdateDTO.getPassword() != null && !doctorUpdateDTO.getPassword().isBlank()) {
            user.setPassword(encryptionService.encode(doctorUpdateDTO.getPassword()));
            userRepo.save(user); // Save updated password in Users table
            isUpdated = true;
        }

        // ✅ Save only if updates were made
        if (isUpdated) {
            doctorRepository.save(doctor);
        } else {
            throw new IllegalArgumentException("No valid fields provided for update.");
        }
    }


}

package com.shubhi.mediease.mapper;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shubhi.mediease.dto.DoctorProfileResponse;
import com.shubhi.mediease.dto.DoctorReq;
import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.entity.Doctors;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.entity.Role;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DoctorMapper {

        public static Users toEntity(DoctorReq doctorResponse, String encryptedPassword) {
            return new Users(
                    null,  // ID will be auto-generated
                    doctorResponse.username(),
                    doctorResponse.email(),
                    encryptedPassword, // Use encrypted password
                    doctorResponse.phone(),
                    Role.DOCTOR, // Set role as DOCTOR,
                    true,
                    0,  // Default failed login attempts
                    false, // Account not locked
                    null,  // CreatedAt will be auto-set
                    null,  // UpdatedAt will be auto-set
                    0  // Version for optimistic locking
            );
        }

    public static DoctorResponse toDto(Users user, String rawPassword) {
        return new DoctorResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                rawPassword  // Return raw password (only for initial registration)
        );
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static DoctorProfileResponse toProfile(Doctors doctor) {
        Map<String, List<String>> scheduleMap = null;

        // Convert JSON schedule string to Map<String, List<String>>
        if (doctor.getSchedule() != null) {
            try {
                scheduleMap = objectMapper.readValue(doctor.getSchedule(), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error parsing doctor's schedule JSON", e);
            }
        }

        return new DoctorProfileResponse(
                doctor.getId(),
                doctor.getUser().getId(), // Extract userId from Users entity
                doctor.getSpecialization(),
                doctor.getExperienceYears(),
                doctor.getQualification(),
                doctor.getConsultationFee(),
                doctor.getHospitalAddress(),
                scheduleMap,
                doctor.isApproved()
        );
    }
    }


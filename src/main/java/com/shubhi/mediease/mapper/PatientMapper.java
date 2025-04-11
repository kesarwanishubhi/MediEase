package com.shubhi.mediease.mapper;

import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.dto.RegisterPatient;

import com.shubhi.mediease.entity.Patients;
import com.shubhi.mediease.entity.Users;

import com.shubhi.mediease.entity.Role;
import lombok.Builder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
@Builder
@Component
public class PatientMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Convert DTO to User entity
    public Users toUserEntity(RegisterPatient dto) {
        return Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password("shubhi")
                .phone(dto.getPhone())
                .role(Role.PATIENT)
                .isActive(true)
                .failedLoginAttempts(0)
                .accountLocked(false)
                .build();
    }

    // Convert DTO to Patient entity
    public Patients toPatientEntity(RegisterPatient dto, Users user) {
        return Patients.builder()
                .user(user)
                .gender(dto.getGender())
                .age(dto.getAge())
                .address(dto.getAddress())
                .bloodGroup(dto.getBloodGroup())
                .emergencyContact(dto.getEmergencyContact())
                .build();
    }
    public static DoctorResponse toDto(Users user) {
        return new DoctorResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword()
                // Return raw password (only for initial registration)
        );
    }

}

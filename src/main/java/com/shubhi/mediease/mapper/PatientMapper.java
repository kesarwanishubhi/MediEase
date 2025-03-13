package com.shubhi.mediease.mapper;

import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.dto.RegisterPatient;

import com.shubhi.mediease.entity.Patients;
import com.shubhi.mediease.entity.Users;

import com.shubhi.mediease.entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
        return new Patients(null, user, dto.getGender(), dto.getAge(), dto.getAddress(),
                dto.getBloodGroup(), dto.getEmergencyContact());
    }
    public static DoctorResponse toDto(Users user) {
        return new DoctorResponse(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword()  // Return raw password (only for initial registration)
        );
    }

}

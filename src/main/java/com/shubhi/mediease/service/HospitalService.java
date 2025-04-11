package com.shubhi.mediease.service;

import com.shubhi.mediease.dto.HospitalCreate;
import com.shubhi.mediease.entity.Hospitals;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.repo.HospitalRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepo hospitalRepository;

    @Transactional
    public Hospitals createHospital(HospitalCreate hospitalDTO) {
        boolean existingUser = hospitalRepository.existsByEmail(hospitalDTO.getEmail());
        if (existingUser) {
            throw new RuntimeException("Hospital with email " + hospitalDTO.getEmail() + " is already registered.");
        }



        // 🔹 Map DTO to Entity using Builder Pattern
        Hospitals hospital = Hospitals.builder()
                .name(hospitalDTO.getName().trim())
                .address(hospitalDTO.getAddress().trim())
                .phoneNumber(hospitalDTO.getPhoneNumber().trim())
                .email(hospitalDTO.getEmail().trim().toLowerCase())
                .build();

        // 🔹 Save to database
        return hospitalRepository.save(hospital);
    }
}

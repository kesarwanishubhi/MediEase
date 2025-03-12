package com.shubhi.mediease.dto;

import java.util.List;
import java.util.Map;

public record DoctorProfileResponse(
        Long id,
        Long userId,  // Foreign key reference to Users table
        String specialization,
        int experienceYears,
        String qualification,
        double consultationFee,
        String hospitalAddress,
        Map<String, List<String>> schedule,
        boolean isApproved
) {}

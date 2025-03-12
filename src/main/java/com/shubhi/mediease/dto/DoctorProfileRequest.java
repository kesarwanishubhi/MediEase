package com.shubhi.mediease.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record DoctorProfileRequest(
        @NotBlank(message = "Specialization is required") String specialization,

        @NotNull(message = "Experience is required")
        @Min(value = 0, message = "Experience cannot be negative") Integer experienceYears,

        @NotBlank(message = "Qualification is required") String qualification,

        @NotNull(message = "Consultation fee is required")
        @Min(value = 0, message = "Fee cannot be negative") Double consultationFee,

        @NotBlank(message = "Hospital address is required") String hospitalAddress,

        @NotNull(message = "Schedule is required") Map<String, List<String>> schedule // Map format (Day -> List of slots)
) {}

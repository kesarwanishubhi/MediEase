package com.shubhi.mediease.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerificationRequest {

    @NotBlank(message = "Doctor email is required")
    @Email(message = "Invalid doctor email format")
    private String doctorEmail;

    @NotBlank(message = "Patient email is required")
    @Email(message = "Invalid email format")
    private String patientEmail;

    @NotBlank(message = "Document name is required")
    private String documentName;

    @NotBlank(message = "OTP is required")
    private String otp;
}

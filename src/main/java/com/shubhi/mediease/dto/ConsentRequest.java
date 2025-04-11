package com.shubhi.mediease.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsentRequest {

    @NotBlank(message = "Doctor email is required")
    @Email(message = "Invalid doctor email format")
    private String doctorEmail;  // Doctor requesting access

    @NotBlank(message = "Patient email is required")
    @Email(message = "Invalid  email format")
    private String patientEmail;  // Doctor requesting access

    @NotBlank(message = "Document name is required")
    private String documentName; // Specific document being requested

    @NotBlank(message = "Hospital name is required")
    private String hospitalName; // Hospital where the doctor is working
}

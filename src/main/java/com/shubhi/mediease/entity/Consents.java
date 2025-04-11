package com.shubhi.mediease.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consents {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Using UUID for unique consent tracking
    private String consentId;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospitals hospital;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patients patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctors doctor; // Doctor receiving consent

    @Column(nullable = false)
    private String reqDoctorId; // Doctor who requested consent

    @Column(nullable = false)
    private String documentName; // The specific document being accessed

    @Column(nullable = false)
    private String otp; // Encrypted OTP for security

    @Column(nullable = false)
    private LocalDateTime otpExpiry; // OTP expiry time

    @Column(nullable = false)
    private Boolean active = true; // Whether the consent is still active

    private LocalDateTime expiryDate; // When document access expires
}

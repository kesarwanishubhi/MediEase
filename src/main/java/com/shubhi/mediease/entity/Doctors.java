package com.shubhi.mediease.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.ConnectionBuilder;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private Users user;  // Foreign Key reference to Users table (id)

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private int experienceYears;

    @Column(nullable = false)
    private String qualification;  // Example: "MBBS, MD"


    @Column(nullable = false)
    private double consultationFee;

    @Column(nullable = false)
    private String hospitalAddress;


    @Column(columnDefinition = "TEXT")
    private String schedule;

    @Column(nullable = false)
    private boolean isApproved = false; // Admin approval required


    @PrePersist //This method is triggered before an entity is saved for the first time
    @PreUpdate //This method is triggered before an entity is updated
    public void validateDoctorRole() {
        if (user.getRole() != Role.DOCTOR) {
            throw new IllegalStateException("User role must be DOCTOR to be in the Doctor table.");
        }
    }
}

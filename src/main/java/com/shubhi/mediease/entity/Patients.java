package com.shubhi.mediease.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private Users user;  // Foreign Key reference to Users table (id)

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = true)
    private String address;



    @Column(nullable = true)
    private String bloodGroup;



    @Column(nullable = true)
    private String emergencyContact; // Contact number of a relative or guardian

    @PrePersist
    @PreUpdate
    public void validatePatientRole() {
        if (user.getRole() != Role.PATIENT) {
            throw new IllegalStateException("User role must be PATIENT to be in the Patient table.");
        }
    }
}



package com.shubhi.mediease.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@Builder
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
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consents> consents; // Consent records associated with this patient

    @PrePersist
    @PreUpdate
    public void validatePatientRole() {
        if (user.getRole() != Role.PATIENT) {
            throw new IllegalStateException("User role must be PATIENT to be in the Patient table.");
        }
    }
}



package com.shubhi.mediease.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hospitals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;  // Unique phone number for contact

    @Column(nullable = false, unique = true)
    private String email;  // Unique email for hospital
//
//    @Column(nullable = false)
//    private String hospitalType; // e.g., General, Multi-specialty, Orthopedic, Pediatric, etc.

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctors> doctors; // One hospital has multiple doctors
}

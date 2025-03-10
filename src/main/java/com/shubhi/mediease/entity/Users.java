package com.shubhi.mediease.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ADMIN, DOCTOR, PATIENT

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; // Soft delete functionality so that in future if we dont want this record so instead of deleting as it would effect other table we only soft delete

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0; // if number of attempts surpass the threshold then block the user for sometime

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false; // Locks account after multiple failed attempts

//    @Column(name = "password_reset_token")
//    private String passwordResetToken; // Token for password recovery
//
//    @Column(name = "password_reset_expiry")
//    private LocalDateTime passwordResetExpiry; // Expiry time for reset token

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Users(Object o, String username, String email, String encryptedPassword, String phone, Role role, boolean active, int i, boolean b, Object o1, Object o2, int i1) {
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}


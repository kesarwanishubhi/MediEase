package com.shubhi.mediease.repo;

import com.shubhi.mediease.entity.Doctors;
import com.shubhi.mediease.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Doctorsrepo extends JpaRepository<Doctors, Long> {

    // Find a doctor by user ID (since each doctor is linked to a user)
    Optional<Doctors> findByUser(Users user);

    // Check if a doctor exists by user ID
    boolean existsByUserId(Users user);
}
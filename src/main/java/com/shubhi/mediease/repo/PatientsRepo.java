package com.shubhi.mediease.repo;

import com.shubhi.mediease.entity.Patients;
import com.shubhi.mediease.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientsRepo extends JpaRepository<Patients, Long> {
    Optional<Patients> findByUser(Users user);
}

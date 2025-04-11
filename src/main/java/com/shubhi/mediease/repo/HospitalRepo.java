package com.shubhi.mediease.repo;

import com.shubhi.mediease.entity.Hospitals;
import com.shubhi.mediease.entity.Patients;
import com.shubhi.mediease.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepo extends JpaRepository<Hospitals, Long> {
    public boolean existsByEmail(String email);
    public Hospitals findByName(String name);
}

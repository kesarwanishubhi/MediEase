package com.shubhi.mediease.repo;

import com.shubhi.mediease.entity.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientsRepo extends JpaRepository<Patients, Long> {
}

package com.shubhi.mediease.repo;

import com.shubhi.mediease.entity.Consents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsentsRepo extends JpaRepository<Consents, Long> {
    Optional<Consents> findByPatientUserEmailAndDoctorUserEmailAndDocumentName(
            String patientEmail, String doctorEmail, String documentName);
}

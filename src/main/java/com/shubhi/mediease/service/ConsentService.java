package com.shubhi.mediease.service;

import com.shubhi.mediease.dto.ConsentRequest;
import com.shubhi.mediease.dto.OtpVerificationRequest;
import com.shubhi.mediease.entity.*;
import com.shubhi.mediease.helper.FileStorageService;
import com.shubhi.mediease.helper.OtpHelper;
import com.shubhi.mediease.helper.ResourceNotFoundException;
import com.shubhi.mediease.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.util.ResourceUtils.getFile;

@Service
@RequiredArgsConstructor
public class ConsentService {
    private final OtpHelper otpHelper;
    private final ConsentsRepo consentRepo;
    private final UsersRepo userRepository;
    private final Doctorsrepo doctorRepository;
    private final PatientsRepo patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final HospitalRepo hospitalRepo;
    private final FileStorageService fileStorageService;
    private final MailService mailService;
    private final PatientService patientService;



    @Transactional
    public ResponseEntity<?> requestConsent(String email, ConsentRequest requestDTO) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch Doctor by Users object
        Doctors requestingDoctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        // Fetch Patient by Users object
        Users patientUser = userRepository.findByEmail(requestDTO.getPatientEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Patients patient = patientRepository.findByUser(patientUser)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Fetch Hospital by Name
        Hospitals hospital = hospitalRepo.findByName(requestDTO.getHospitalName());
//        if (!patientService.getFile(patientUser.getEmail(), requestDTO.getDocumentName())) {
//            return ResponseEntity.badRequest().body("Requested document not found");
//        }

        String otp = otpHelper.generateOtp();
        Consents consent = Consents.builder()
                .patient(patient)
                .doctor(requestingDoctor)
                .hospital(hospital)
                .documentName(requestDTO.getDocumentName())
                .otp(otp)
                .reqDoctorId(requestingDoctor.getId().toString())
                .otpExpiry(otpHelper.generateExpirationTime())
                .active(true)
                .build();

        consentRepo.save(consent);

        String msg = "Dr. " + user.getUsername() + " is requesting access to your document: " +
                requestDTO.getDocumentName() + ". Use OTP " + otp + " to grant access. OTP is valid for 10 minutes.";

        mailService.sendEmail(patientUser.getEmail(), "Consent Request for Document", msg);

        return ResponseEntity.ok("Consent request sent successfully");
    }


    public boolean verifyOtp(OtpVerificationRequest request) {

        Optional<Consents> consentOpt = consentRepo.findByPatientUserEmailAndDoctorUserEmailAndDocumentName(
                request.getPatientEmail(), request.getDoctorEmail(), request.getDocumentName()
        );

        // Consent request does not exist
        if (consentOpt==null) {
            throw new IllegalArgumentException("No pending consent request found for the given details.");
        }

        Consents consent = consentOpt.get();

        // Consent is already active
        if (consent.getActive()) {
            throw new IllegalStateException("Access for this document is already granted.");
        }


        if (!OtpHelper.otpCheck(consent.getOtp(), request.getOtp(), consent.getOtpExpiry())) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        //  Activate consent for document access
        consent.setActive(true);
        consentRepo.save(consent);

        //Notify the doctor via email
        String doctorEmail = request.getDoctorEmail();
        String patientEmail = request.getPatientEmail();
        String docName = request.getDocumentName();
        String message = String.format("Dear Doctor,\n\nAccess to the document '%s' of patient %s has been successfully verified.\n\nYou may now proceed with accessing the document.\n\nBest regards,\nMediEase Team", docName, patientEmail);

        mailService.sendEmail(doctorEmail, "Document Access Verified", message);

        return true;
    }

}

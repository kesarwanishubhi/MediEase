package com.shubhi.mediease.service;

import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.dto.FileUploadRequest;
import com.shubhi.mediease.dto.RegisterPatient;
import com.shubhi.mediease.entity.Patients;
import com.shubhi.mediease.entity.Role;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.helper.EncryptionFileService;
import com.shubhi.mediease.helper.FileStorageService;
import com.shubhi.mediease.mapper.DoctorMapper;
import com.shubhi.mediease.mapper.PatientMapper;
import com.shubhi.mediease.repo.PatientsRepo;
import com.shubhi.mediease.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PatientService {
    private static final String STORAGE_PATH = "C:\\MEDIEASE\\PATIENT"; // Change this path as needed
    private static final int MAX_FILES = 20;


    private final PatientsRepo patientRepository;
    private final UsersRepo userRepository;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final FileStorageService fileStorageService;
    private final EncryptionFileService encryptionService;
    public DoctorResponse registerPatient(RegisterPatient dto) {
        // Check if user with email already exists
        Optional<Users> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Doctor with email " + dto.getEmail() + " is already registered.");
        }


        // Save user first
        Users newUser = userRepository.save(patientMapper.toUserEntity(dto));

        // Save patient details

        Patients newPatient = patientMapper.toPatientEntity(dto, newUser);
        patientRepository.save(newPatient);


        // Return response as DTO
        return patientMapper.toDto(newUser);

    }
    /**
     * Store encrypted file under the respective patient's folder.
     */


    public String uploadFile(String email, MultipartFile file) throws Exception {
        Patients patient = getPatientByEmail(email);
        if (patient == null) {
            return "Patient not found.";
        }

        String folderPath = STORAGE_PATH + patient.getId();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Check file limit
        int totalFiles = getAllFileNames(email).size();
        if (totalFiles >= MAX_FILES) {
            return "Cannot store more than " + MAX_FILES + " files.";
        }

        File storedFile = new File(folder, file.getOriginalFilename());

        if (storedFile.exists()) {
            return "File " + file.getOriginalFilename() + " already exists.";
        }

        try (InputStream inputStream = file.getInputStream()) {
            encryptionService.encryptAndStoreFile(storedFile.getAbsolutePath(), inputStream);
        }

        return "File uploaded successfully.";
    }



    /**
     * Get decrypted file contents.
     */
    public byte[] getFile(String email, String fileName) throws Exception {

        Patients patient = getPatientByEmail(email);
        if (patient == null) {
            throw new RuntimeException("Patient not found.");
        }

        String filePath = STORAGE_PATH + patient.getId() + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found.");
        }

        return encryptionService.decryptFile(filePath);
    }


    /**
     * Get list of all files in the patient's directory.
     */
    public List<String> getAllFileNames(String email) {
        // Retrieve patient details using email
        Patients patient = getPatientByEmail(email);
        if (patient == null) {
            return List.of("Patient not found.");
        }

        // Construct the patient's folder path
        String folderPath = STORAGE_PATH + patient.getId();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            return Arrays.stream(files)
                    .filter(File::isFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
        }
        return List.of("No files found.");
    }


    /**
     * Delete file securely.
     */
    public String deleteFile(String email, String fileName) {


        Patients patient = getPatientByEmail(email);
        if (patient == null) {
            return "Patient not found.";
        }

        String filePath = STORAGE_PATH + patient.getId() + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            return "File not found.";
        }

        return file.delete() ? "File deleted successfully." : "Failed to delete file.";
    }


    /**
     * Retrieve Patient using User email.
     */
    public Patients getPatientByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException("The user with email " + email + " is not a patient.");
        }

        return patientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No patient record found for user ID: " + user.getId()));
    }
}
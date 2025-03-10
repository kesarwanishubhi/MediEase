package com.shubhi.mediease.mapper;




import com.shubhi.mediease.dto.DoctorResponse;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

        public static Users toEntity(DoctorResponse doctorResponse, String encryptedPassword) {
            return new Users(
                    null,  // ID will be auto-generated
                    doctorResponse.username(),
                    doctorResponse.email(),
                    encryptedPassword, // Use encrypted password
                    doctorResponse.phone(),
                    Role.DOCTOR, // Set role as DOCTOR,
                    true,
                    0,  // Default failed login attempts
                    false, // Account not locked
                    null,  // CreatedAt will be auto-set
                    null,  // UpdatedAt will be auto-set
                    0  // Version for optimistic locking
            );
        }

        public static DoctorResponse toDto(Users user, String rawPassword) {
            return new DoctorResponse(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone()  // Return raw password (only for initial registration)
            );
        }
    }


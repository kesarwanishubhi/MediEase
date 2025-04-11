package com.shubhi.mediease.helper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OtpHelper {

    private static final SecureRandom random = new SecureRandom();
    private PasswordEncoder passwordEncoder;


    /**
     * Generates a 6-digit OTP.
     */
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    /**
     * Generates OTP expiration time (Default: 10 minutes).
     */
    public LocalDateTime generateExpirationTime() {
        return LocalDateTime.now().plus(10, ChronoUnit.MINUTES);
    }

    /**
     * Generates expiration time for granted consent based on minutes.
     */
    public LocalDateTime generateExpirationTime(int minutes) {
        return LocalDateTime.now().plus(minutes, ChronoUnit.MINUTES);
    }

    /**
     * Verifies if OTP is correct and not expired.
     */
    public static boolean otpCheck(String providedOtp, String storedOtp, LocalDateTime expiryTime) {
        if (storedOtp == null || expiryTime == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(expiryTime)) {
            return false; // OTP expired
        }
        return providedOtp.equals(storedOtp);
    }

}

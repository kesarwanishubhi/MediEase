package com.shubhi.mediease.helper;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Generator {

    private static final String ENV_FILE_PATH = ".env"; // Path to .env file

    public static String generateAESKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256, new SecureRandom());  // Ensure 256-bit key
            SecretKey secretKey = keyGenerator.generateKey();

            // Directly return the key as a Base64 string
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // Check if AES key already exists
        String existingAESKey = dotenv.get("AES_ENCRYPTION_KEY");

        if (existingAESKey != null) {
            System.out.println("AES Key already exists. Skipping generation.");
            return; // Exit if key already exists
        }

        // Generate new AES key
        String aesKey = generateAESKey();

        // Append key to .env file
        try (FileWriter writer = new FileWriter(ENV_FILE_PATH, true)) {
            writer.write("\nAES_ENCRYPTION_KEY=" + aesKey);
            System.out.println("AES Key generated and saved to .env file.");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to .env file", e);
        }
    }
}

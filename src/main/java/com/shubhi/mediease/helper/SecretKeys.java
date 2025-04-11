package com.shubhi.mediease.helper;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class SecretKeys {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String AES_ENCRYPTION_KEY = dotenv.get("AES_ENCRYPTION_KEY");
    public static final String EMAIL = dotenv.get("EMAIL");
    public static final String PASSWORD = dotenv.get("PASSWORD");
}

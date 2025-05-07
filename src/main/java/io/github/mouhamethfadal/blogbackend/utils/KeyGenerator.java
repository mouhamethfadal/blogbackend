package io.github.mouhamethfadal.blogbackend.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class KeyGenerator {
    public static void main(String[] args) {
        generateAndPrintKey();
    }

    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void generateAndPrintKey() {
        String secretKey = generateKey();

        log.info("\n===== JWT KEY GENERATOR =====");
        log.info("Generated Key: {}", secretKey);
        log.info("Add this to your environment variables:");
        log.info("JWT_SECRET={}", secretKey);
        log.info("===============================\n");
    }
}

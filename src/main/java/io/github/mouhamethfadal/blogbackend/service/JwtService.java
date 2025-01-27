package io.github.mouhamethfadal.blogbackend.service;


import org.springframework.security.core.Authentication;

public interface JwtService {
    /**
     * Generate a JWT token from authentication details
     * @param authentication the authentication object
     * @return the generated JWT token
     */
    String generateToken(Authentication authentication);

    /**
     * Extract username from JWT token
     * @param token the JWT token
     * @return the username
     */
    String getUsernameFromToken(String token);

    /**
     * Validate a JWT token
     * @param token the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    boolean validateToken(String token);
}

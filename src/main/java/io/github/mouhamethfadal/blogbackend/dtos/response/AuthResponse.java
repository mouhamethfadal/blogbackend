package io.github.mouhamethfadal.blogbackend.dtos.response;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for authentication responses.
 * Returned after successful login or registration operations.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code token} - The JWT authentication token for the authenticated user (should be included in subsequent requests)</li>
 *   <li>{@code username} - The username of the authenticated user</li>
 * </ul>
 */
@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
}

package io.github.mouhamethfadal.blogbackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login requests.
 * Contains the credentials required for user authentication.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code username} - The username for authentication (must not be blank)</li>
 *   <li>{@code password} - The password for authentication (must not be blank)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

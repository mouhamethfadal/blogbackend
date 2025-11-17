package io.github.mouhamethfadal.blogbackend.dtos.request;

import io.github.mouhamethfadal.blogbackend.validations.annotation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration requests.
 * Contains user credentials and contact information required for creating a new account.
 * All fields are validated to ensure data integrity.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code username} - The desired username for the new account (must not be blank)</li>
 *   <li>{@code email} - The email address for the new account (must not be blank and must be valid email format)</li>
 *   <li>{@code password} - The password for the new account (must have 8+ characters with at least 1 uppercase, 1 lowercase, 1 number, 1 special character)</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username should not be blank")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @StrongPassword(message = "Password must have 8+ characters with at least: 1 uppercase, 1 lowercase, 1 number, 1 special char")
    private String password;
}

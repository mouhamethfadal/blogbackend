package io.github.mouhamethfadal.blogbackend.dtos.request;

import io.github.mouhamethfadal.blogbackend.validations.annotation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

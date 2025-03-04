package io.github.mouhamethfadal.blogbackend.dtos;

import io.github.mouhamethfadal.blogbackend.validations.annotation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username should not be blank")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @StrongPassword(message = "Password must be at least 8 characters long and contain uppercase, lowercase, number, special character")
    private String password;
}

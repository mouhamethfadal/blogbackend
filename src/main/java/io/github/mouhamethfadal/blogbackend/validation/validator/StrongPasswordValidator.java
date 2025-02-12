package io.github.mouhamethfadal.blogbackend.validation.validator;

import io.github.mouhamethfadal.blogbackend.validation.annotation.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password == null) {
            return false;
        }

        // Check if password has minimum length of 8 characters
        boolean hasLength = password.length() >= 8;

        // Check if password has at least one uppercase letter
        boolean hasUppercase = password.matches(".*[A-Z].*");

        // Check if password has at least one lowercase letter
        boolean hasLowercase = password.matches(".*[a-z].*");

        // Check if password has at least one digit
        boolean hasDigit = password.matches(".*\\d.*");

        // Check if password has at least one special character
        boolean hasSpecialChar = password.matches(".*[@#$%^&+=].*");

        return hasLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
}

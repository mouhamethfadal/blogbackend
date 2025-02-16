package io.github.mouhamethfadal.blogbackend.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {
    @Mock
    private ConstraintValidatorContext context;

    private StrongPasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StrongPasswordValidator();
    }

    @Nested
    @DisplayName("Basic validation tests")
    class BasicValidationTests {
        @Test
        void isValid_WhenPasswordIsNull_ShouldReturnFalse() {
            assertThat(validator.isValid(null, context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordIsTooShort_ShouldReturnFalse() {
            assertThat(validator.isValid("pass", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordHasNoUppercase_ShouldReturnFalse() {
            assertThat(validator.isValid("averylongpassword", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordHasNoLowercase_ShouldReturnFalse() {
            assertThat(validator.isValid("AVERYLONGPASSWORD", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordHasNoDigits_ShouldReturnFalse() {
            assertThat(validator.isValid("aVERYlongpassword", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordHasNoSpecialChars_ShouldReturnFalse() {
            assertThat(validator.isValid("aVERYlongpassword123", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordMeetsAllCriteria_ShouldReturnTrue() {
            assertThat(validator.isValid("aVALIDlongPassword1234@#", context)).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCasesTests {
        @Test
        void isValid_WhenPasswordIsEmpty_ShouldReturnFalse() {
            assertThat(validator.isValid("", context)).isFalse();
        }

        @Test
        void isValid_WhenPasswordMeetsMinimumRequirements_ShouldReturnTrue() {
            assertThat(validator.isValid("ABcd123+", context)).isTrue();
        }
    }

}

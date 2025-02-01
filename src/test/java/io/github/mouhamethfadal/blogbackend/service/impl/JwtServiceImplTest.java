package io.github.mouhamethfadal.blogbackend.service.impl;

import io.github.mouhamethfadal.blogbackend.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @Mock (strictness = Mock.Strictness.LENIENT)
    private JwtProperties jwtProperties;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private Authentication authentication;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String DIFFERENT_SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5971";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final long EXPIRATION_TIME = 3600; // 1 hour in seconds

    @BeforeEach
    void setUp() {
        UserDetails userDetails = new User(USERNAME, PASSWORD, Collections.emptyList());
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtProperties.getSecret()).thenReturn(SECRET_KEY);
        when(jwtProperties.getExpirationInSeconds()).thenReturn(EXPIRATION_TIME);
    }

    @Nested
    @DisplayName("Token Generation Tests")
    class GenerateTokenTests {
        @Test
        @DisplayName("Should generate valid token with correct claims")
        void shouldGenerateValidTokenWithCorrectClaims() {
            // Act
            String token = jwtService.generateToken(authentication);

            // Assert
            assertThat(token).isNotNull().isNotBlank();
            assertThat(jwtService.validateToken(token)).isTrue();
            assertThat(jwtService.getUsernameFromToken(token)).isEqualTo(USERNAME);
        }
        
    }

    @Test
    @DisplayName("Should throw an exception when authentication is null")
    void shouldThrowExceptionWhenAuthenticationIsNull() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(null);

        // Act and Assert
        assertThatThrownBy(() -> jwtService.generateToken(authentication)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        // Arrange
        UserDetails user1 = new User("user1", "password1", Collections.emptyList());
        UserDetails user2 = new User("user2", "password2", Collections.emptyList());

        // Act
        when(authentication.getPrincipal()).thenReturn(user1);
        String token1 = jwtService.generateToken(authentication);

        when(authentication.getPrincipal()).thenReturn(user2);
        String token2 = jwtService.generateToken(authentication);

        // Assert
        assertThat(token1).isNotEqualTo(token2);
    }

    @Nested
    @DisplayName("Token validation tests")
    class TokenValidationTests {
        @Test
        @DisplayName("Should validate correct token")
        void shouldValidateCorrectToken() {
            // Act
            String token = jwtService.generateToken(authentication);

            // Assert
            assertThat(jwtService.validateToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should return false when token is expired")
        void shouldRejectExpiredToken() {
            // Arrange
            when(jwtProperties.getExpirationInSeconds()).thenReturn(1L);

            // Act
            String token = jwtService.generateToken(authentication);
            try {
               Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Assert
            assertThat(jwtService.validateToken(token)).isFalse();
        }

        @Test
        @DisplayName("Should return false when token is malformed")
        void shouldRejectMalformedToken() {
            // Act and Assert
            assertThat(jwtService.validateToken("malformed.token")).isFalse();
        }

        @Test
        @DisplayName("Should return false when token is empty")
        void shouldRejectEmptyToken() {
            // Act and Assert
            assertThat(jwtService.validateToken("")).isFalse();
        }

        @Test
        @DisplayName("Should return false when token is null")
        void shouldRejectNullToken() {
            assertThat(jwtService.validateToken(null)).isFalse();
        }

        @Test
        @DisplayName("Should return false when token has invalid signature")
        void shouldRejectInvalidSignature() {
            // Arrange
            String token = jwtService.generateToken(authentication);
            when(jwtProperties.getSecret()).thenReturn(DIFFERENT_SECRET_KEY);

            // Act and Assert
            assertThat(jwtService.validateToken(token)).isFalse();
        }

        @Test
        @DisplayName("Should return false when token is unsupported")
        void shouldRejectUnsupportedToken() {
            // Arrange
            String token = "eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0." +
                    "eyJzdWIiOiJ1c2VyIiwiZXhwIjo5OTk5OTk5OTk5fQ.";

            // Act and Assert
            assertThat(jwtService.validateToken(token)).isFalse();
        }

        @Test
        @DisplayName("Should return false for generic exception")
        void shouldReturnFalseForGenericException() {
            // Arrange
            when(jwtProperties.getSecret()).thenThrow(new RuntimeException());

            // Act and Assert
            assertThat(jwtService.validateToken("random.token.here")).isFalse();
        }
    }

    @Nested
    @DisplayName("Username extraction tests")
    class UsernameExtractionTests {
        @Test
        @DisplayName("Should extract correct username from valid token")
        void shouldExtractCorrectUsernameFromValidToken() {
            // Act
            String token = jwtService.generateToken(authentication);

            // Assert
            assertThat(jwtService.validateToken(token)).isTrue();
            assertThat(jwtService.getUsernameFromToken(token)).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("Should throw exception when extracting username for invalid token")
        void shouldThrowExceptionWhenExtractingUsernameFromInvalidToken() {
            // Act and Assert
            assertThatThrownBy(() -> jwtService.getUsernameFromToken("invalid.token")).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should throw exception when extracting username for null token")
        void shouldThrowExceptionWhenExtractingUsernameFromNullToken() {
            // Act and Assert
            assertThatThrownBy(() -> jwtService.getUsernameFromToken(null)).isInstanceOf(IllegalArgumentException.class);
        }
    }

}

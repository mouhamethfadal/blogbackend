package io.github.mouhamethfadal.blogbackend.service.impl;

import io.github.mouhamethfadal.blogbackend.config.JwtProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final String USERNAME = "testUser";
    private static final long EXPIRATION_TIME = 3600; // 1 hour in seconds

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn(SECRET_KEY);
        when(jwtProperties.getExpirationInSeconds()).thenReturn(EXPIRATION_TIME);
    }

    @Nested
    @DisplayName("Token Generation Tests")
    class GenerateTokenTests {
        
    }

}

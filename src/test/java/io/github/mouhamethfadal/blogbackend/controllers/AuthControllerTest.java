package io.github.mouhamethfadal.blogbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mouhamethfadal.blogbackend.dtos.request.LoginRequest;
import io.github.mouhamethfadal.blogbackend.dtos.request.RegisterRequest;
import io.github.mouhamethfadal.blogbackend.dtos.response.AuthResponse;
import io.github.mouhamethfadal.blogbackend.exceptions.EmailAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.exceptions.UsernameAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.security.JwtAuthenticationFilter;
import io.github.mouhamethfadal.blogbackend.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setUsername("testUser");
        validRegisterRequest.setPassword("testPassword@123");
        validRegisterRequest.setEmail("testEmail@example.com");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("testUser");
        validLoginRequest.setPassword("testPassword@123");

        authResponse = AuthResponse.builder()
                .username("testUser")
                .token("eyJhdVi3pmA4d...")
                .build();
    }

    @Nested
    @DisplayName("User Registration Tests")
    class UserRegistrationTests {
        @Test
        void register_WhenUserSendsValidRequest_ShouldReturnSuccessWithToken() throws Exception {
            // Arrange
            when(authService.register(validRegisterRequest)).thenReturn(authResponse);

            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(("$.token")).value(authResponse.getToken()))
                    .andExpect(jsonPath("$.username").value(validRegisterRequest.getUsername()));

        }

        @Test
        void register_WhenUsernameAlreadyExists_ShouldReturnConflict() throws Exception {
            // Arrange
            when(authService.register(validRegisterRequest)).thenThrow(new UsernameAlreadyExistsException(validRegisterRequest.getUsername()));

            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath(("$.message")).value("Username already exists: " + validRegisterRequest.getUsername()));

        }
        @Test
        void register_WhenEmailAlreadyExists_ShouldReturnConflict() throws Exception {
            // Arrange
            when(authService.register(validRegisterRequest)).thenThrow(new EmailAlreadyExistsException(validRegisterRequest.getEmail()));

            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath(("$.message")).value("Email already exists: " + validRegisterRequest.getEmail()));

        }
        
        @Test
        void register_WhenPasswordIsWeak_ShouldReturnBadRequest() throws Exception {
            // Arrange
            RegisterRequest invalidRegisterRequest = validRegisterRequest;
            invalidRegisterRequest.setPassword("passer");

            // Act and Assert
            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath(("$.message")).value("Password must have 8+ characters with at least: 1 uppercase, 1 lowercase, 1 number, 1 special char"));

        }
    }

    @Nested
    @DisplayName("User Login Tests")
    class UserLoginTests {
        @Test
        void login_WhenUserSendsValidRequest_ShouldReturnSuccessWithToken() throws Exception {
            // Arrange
            when(authService.login(validLoginRequest)).thenReturn(authResponse);

            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(("$.token")).value(authResponse.getToken()))
                    .andExpect(jsonPath("$.username").value(validLoginRequest.getUsername()));

        }

        @Test
        void login_WhenUserProvidesBadCredentials_ShouldReturnUnauthorized() throws Exception {
            // Arrange
            when(authService.login(validLoginRequest)).thenThrow(new BadCredentialsException("Bad credentials"));

            // Act and Assert
            // Act and Assert
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath(("$.message")).value("Bad credentials"));
        }
    }


}



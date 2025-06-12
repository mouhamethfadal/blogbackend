package io.github.mouhamethfadal.blogbackend.controllers;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
import io.github.mouhamethfadal.blogbackend.security.JwtAuthenticationFilter;
import io.github.mouhamethfadal.blogbackend.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @MockitoBean
    private UserService userService;

    UserDto user1 = UserDto.builder()
            .username("john_doe")
            .email("john_doe@gmail.com")
            .roles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN))
            .build();

    UserDto user2 = UserDto.builder()
            .username("ricky")
            .email("ricky@gmail.com")
            .roles(Set.of(Role.ROLE_USER))
            .build();


    @Nested
    @DisplayName("Test cases for getAllUsers")
    class GetAllUsersTests{
        @Test
        void getAllUsers_WhenUsersExist_ShouldReturnAllUsers() throws Exception {
            // Arrange
            when(userService.findAllUsers()).thenReturn(List.of(user1, user2));

            // Act & Assert
           mockMvc.perform(get("/api/v1/users"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$", hasSize(2)))
                   .andExpect(jsonPath("$[*].username", containsInAnyOrder(user1.getUsername(), user2.getUsername())));

        }

        @Test
        void getAllUsers_WhenNoUserExist_ShouldReturnEmptyList() throws Exception {
            // Arrange
            when(userService.findAllUsers()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/v1/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

    }

    @Nested
    @DisplayName("getUserByUsername Test Cases")
    class GetUserByUsernameTests{
        @Test
        void getUserByUsername_WhenUserExist_ShouldReturnUser() throws Exception {
            // Arrange
            when(userService.findUserByUsername(user1.getUsername())).thenReturn(user1);

            // Act & Assert
            mockMvc.perform(get("/api/v1/users/" + user1.getUsername()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username", is(user1.getUsername())));


        }
        @Test
        void getUserByUsername_WhenNoUserExist_ShouldThrowException() throws Exception {
            // Arrange
            when(userService.findUserByUsername(user1.getUsername())).thenThrow(new UserNotFoundException(user1.getUsername()));

            // Act & Assert
            mockMvc.perform(get("/api/v1/users/" + user1.getUsername()))
                    .andExpect(status().isNotFound());
        }

    }

}

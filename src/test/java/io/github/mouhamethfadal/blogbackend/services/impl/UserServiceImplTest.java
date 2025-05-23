package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
import io.github.mouhamethfadal.blogbackend.mappers.UserMapper;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();

        user1 = User.builder()
                .username("testUser")
                .email("testUser@test.com")
                .password("test123")
                .roles(roles)
                .build();
        user2 = User.builder()
                .username("testUser2")
                .email("testUser2@test.com")
                .password("test123")
                .roles(roles)
                .build();

        userDto1 = UserDto.builder()
                .username(user1.getUsername())
                .email(user1.getEmail())
                .roles(user1.getRoles())
                .build();

        userDto2 = UserDto.builder()
                .username(user2.getUsername())
                .email(user2.getEmail())
                .roles(user2.getRoles())
                .build();
    }

    @Nested
    @DisplayName("findAllUsers Test Cases")
    class findAllUsersTests {
        @Test
        void findAllUsers_WhenUsersExist_ShouldReturnAllUsers() {
            // Arrange
            when(userRepository.findAll()).thenReturn(List.of(user1, user2));
            when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
            when(userMapper.userToUserDto(user2)).thenReturn(userDto2);

            // Act
            List<UserDto> users = userService.findAllUsers();

            assertThat(users)
                    .hasSize(2)
                    .containsExactlyInAnyOrder(userDto1, userDto2);
        }

        @Test
        void findAllUsers_WhenNoUsersExist_ShouldReturnEmptyList() {
            // Arrange
            when(userRepository.findAll()).thenReturn(List.of());

            // Act
            List<UserDto> users = userService.findAllUsers();

            // Assert
            assertThat(users).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByUsername Test Cases")
    class findByUsernameTests {
        @Test
        void findByUsername_WhenUserExists_ShouldReturnUser() {
            // Arrange
            when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
            when(userMapper.userToUserDto(user1)).thenReturn(userDto1);

            // Act
            UserDto user = userService.findUserByUsername(user1.getUsername());

            // Assert

            assertThat(user).isEqualTo(userDto1);
        }

        @Test
        void findByUsername_WhenNoUserExists_ShouldThrowUserNotFoundException() {
            // Arrange

            String username = user1.getUsername();
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userService.findUserByUsername(username))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("User with username: " + username + " not found");
        }
    }
}

package io.github.mouhamethfadal.blogbackend.service.impl;

import io.github.mouhamethfadal.blogbackend.model.User;
import io.github.mouhamethfadal.blogbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        User user = User.builder()
                .username(username)
                .password(password)
                .roles(List.of("USER", "ADMIN"))
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        String username = "nonExistingUsername";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found");
    }

    @Test
    void loadUserByUsername_WhenUserHasNoRole_ShouldReturnUserDetailsWithNoRole() {
        // Arrange
        String username  = "userWithNoRole";
        String password = "testPassword";

        User user = User.builder()
                .username(username)
                .password(password)
                .roles(List.of())
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities()).isEmpty();
    }

    @Test
    void loadUserByUsername_WhenUserHasOneRole_ShouldReturnUserDetailsWithOneRole() {
        // Arrange
        String username = "userWithOneRole";
        String password = "testPassword";

        User user = User.builder()
                .username(username)
                .password(password)
                .roles(List.of("USER"))
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_USER");
    }

    @Test
    void loadUserByUsername_WhenUserNameIsNull_ShouldThrowException() {
        // Act && Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found");
    }
}

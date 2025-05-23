package io.github.mouhamethfadal.blogbackend.repositories;

import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class UserRepositoryIT {
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

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
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void save_WithValidUser_ShouldSaveUser() {
        User savedUser = userRepository.save(user1);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(user1.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    void findByUsername_WithValidUser_ShouldReturnUser() {
        userRepository.save(user1);

        Optional<User> foundUser = userRepository.findByUsername(user1.getUsername());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user1.getUsername());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    void findByUserName_WithNonExistentUser_ShouldReturnEmpty() {
        Optional<User> foundUser = userRepository.findByUsername("nonExistentUser");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    void findAllUsers_ShouldReturnsAllUsers() {
        // Arrange
        userRepository.saveAll(List.of(user1, user2));

        // Act
        List<User> users = userRepository.findAll();

        // Assert
        assertThat(users).hasSize(2).containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    void existsByUsername_WithValidUser_ShouldReturnTrue() {
        userRepository.save(user1);

        boolean exists = userRepository.existsByUsername(user1.getUsername());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_WithNonExistentUser_ShouldReturnFalse() {
        boolean exists = userRepository.existsByUsername("nonExistentUserName");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WithValidUser_ShouldReturnTrue() {
        userRepository.save(user1);

        boolean exists = userRepository.existsByEmail(user1.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WithNonExistentUser_ShouldReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonExistentEmail");

        assertThat(exists).isFalse();
    }
}

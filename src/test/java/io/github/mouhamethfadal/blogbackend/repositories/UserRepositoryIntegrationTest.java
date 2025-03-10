package io.github.mouhamethfadal.blogbackend.repositories;

import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        Set<Role> roles = new HashSet<>();

        testUser = User.builder()
                .username("testUser")
                .email("testUser@test.com")
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
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void findByUsername_WithValidUser_ShouldReturnUser() {
        userRepository.save(testUser);

        Optional<User> foundUser = userRepository.findByUsername(testUser.getUsername());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(foundUser.get().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void findByUserName_WithNonExistentUser_ShouldReturnEmpty() {
        Optional<User> foundUser = userRepository.findByUsername("nonExistentUser");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    void existsByUsername_WithValidUser_ShouldReturnTrue() {
        userRepository.save(testUser);

        boolean exists = userRepository.existsByUsername(testUser.getUsername());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_WithNonExistentUser_ShouldReturnFalse() {
        boolean exists = userRepository.existsByUsername("nonExistentUserName");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WithValidUser_ShouldReturnTrue() {
        userRepository.save(testUser);

        boolean exists = userRepository.existsByEmail(testUser.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WithNonExistentUser_ShouldReturnFalse() {
        boolean exists = userRepository.existsByEmail("nonExistentEmail");

        assertThat(exists).isFalse();
    }
}

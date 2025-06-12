package io.github.mouhamethfadal.blogbackend.mappers;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperIT {
    @Autowired
    private UserMapper userMapper;
    private User user;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();

        user = User.builder()
                .username("testUser")
                .email("testUser@test.com")
                .password("test123")
                .roles(roles)
                .build();
    }

    @Test
    void userToUserDto_WhenUserIsValid_ShouldCorrectlyMapUser() {
        // Act
        UserDto userDto = userMapper.userToUserDto(user);

        // Assert
        assertThat(userDto)
                .isNotNull()
                .extracting(
                        UserDto::getUsername,
                        UserDto::getEmail,
                        UserDto::getRoles
                )
                .containsExactly(
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles()
                );

        Field[] fields  = UserDto.class.getDeclaredFields();
        List<String> fieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .toList();

        assertThat(fieldNames)
                .doesNotContain("password")
                .containsExactlyInAnyOrder("username", "email", "enabled", "roles");

    }
}
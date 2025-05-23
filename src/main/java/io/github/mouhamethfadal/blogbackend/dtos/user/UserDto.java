package io.github.mouhamethfadal.blogbackend.dtos.user;

import io.github.mouhamethfadal.blogbackend.entities.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private Set<Role> roles;
}

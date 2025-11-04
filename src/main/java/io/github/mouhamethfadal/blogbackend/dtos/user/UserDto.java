package io.github.mouhamethfadal.blogbackend.dtos.user;

import io.github.mouhamethfadal.blogbackend.entities.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Data Transfer Object for user information.
 * Represents user data for responses, excluding sensitive information like passwords.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code username} - The username of the user</li>
 *   <li>{@code email} - The email address of the user</li>
 *   <li>{@code enabled} - Indicates whether the user account is enabled and active</li>
 *   <li>{@code roles} - The set of roles assigned to the user</li>
 * </ul>
 */
@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private boolean enabled;
    private Set<Role> roles;
}

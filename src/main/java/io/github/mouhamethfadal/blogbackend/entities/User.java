package io.github.mouhamethfadal.blogbackend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity representing a user account in the blog system.
 * This entity is stored in the "users" MongoDB collection and includes
 * authentication credentials, contact information, and role assignments.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the user</li>
 *   <li>{@code username} - The username used for authentication and display purposes</li>
 *   <li>{@code email} - The email address associated with the user account</li>
 *   <li>{@code password} - The hashed password for user authentication (should never be exposed in responses)</li>
 *   <li>{@code enabled} - Indicates whether the user account is enabled and active (disabled accounts cannot authenticate)</li>
 *   <li>{@code roles} - The set of roles assigned to the user, determining permissions (defaults to empty HashSet)</li>
 * </ul>
 */
@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseAuditableEntity {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}

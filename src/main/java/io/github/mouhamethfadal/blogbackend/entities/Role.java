package io.github.mouhamethfadal.blogbackend.entities;

/**
 * Enumeration of user roles in the blog application.
 * Defines the different permission levels available for users.
 *
 * <p>Available roles:</p>
 * <ul>
 *   <li>{@code ROLE_ADMIN} - Administrator role with full system access and permissions</li>
 *   <li>{@code ROLE_USER} - Standard user role with basic access permissions</li>
 * </ul>
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_USER
}

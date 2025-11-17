package io.github.mouhamethfadal.blogbackend.dtos.user;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for author information in blog posts.
 * Contains minimal user information to be displayed with posts.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code username} - The username of the author</li>
 *   <li>{@code email} - The email address of the author</li>
 * </ul>
 */
@Data
@Builder
public class AuthorDto {
    private String username;
    private String email;
}

package io.github.mouhamethfadal.blogbackend.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for blog post creation and update requests.
 * Contains all the required and optional information for creating or modifying a blog post.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code title} - The title of the blog post (must not be blank)</li>
 *   <li>{@code description} - A brief description or excerpt of the post (must not be blank)</li>
 *   <li>{@code content} - The main content body of the blog post (must not be blank)</li>
 *   <li>{@code tags} - Optional list of tags for categorizing the post</li>
 * </ul>
 */
@Data
@Builder
public class PostRequestDto {
    @NotBlank(message = "Title should not be blank")
    private String title;
    @NotBlank(message = "Description should not be blank")
    private String description;
    @NotBlank(message = "Content should not be blank")
    private String content;
    private List<String> tags;
}

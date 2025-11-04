package io.github.mouhamethfadal.blogbackend.dtos.post;

import io.github.mouhamethfadal.blogbackend.dtos.user.AuthorDto;
import io.github.mouhamethfadal.blogbackend.enums.PostStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for blog post responses.
 * Contains all post information including metadata for display in the frontend.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier of the post</li>
 *   <li>{@code title} - The title of the blog post</li>
 *   <li>{@code description} - A brief description or excerpt of the post</li>
 *   <li>{@code content} - The main content body of the blog post</li>
 *   <li>{@code slug} - URL-friendly version of the title used in permalinks</li>
 *   <li>{@code author} - Information about the author who created this post</li>
 *   <li>{@code status} - The publication status of the post (e.g., DRAFT, PUBLISHED)</li>
 *   <li>{@code tags} - List of tags associated with the post for categorization</li>
 *   <li>{@code publishedDate} - The date and time when the post was published (may be null if unpublished)</li>
 * </ul>
 */
@Data
@Builder
public class PostResponseDto {
    private String id;
    private String title;
    private String description;
    private String content;
    private String slug;
    private AuthorDto author;
    private PostStatus status;
    private List<String> tags;
    private LocalDateTime publishedDate;

}

package io.github.mouhamethfadal.blogbackend.entities;

import io.github.mouhamethfadal.blogbackend.enums.PostStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Post entity representing a blog post in the system.
 * This entity is stored in the "posts" MongoDB collection and includes
 * full-text search indexing on title and description fields.
 *
 * <p>Fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the post</li>
 *   <li>{@code title} - The title of the blog post (indexed with weight 10 for full-text search)</li>
 *   <li>{@code description} - A brief description or excerpt of the post (indexed with weight 5 for full-text search)</li>
 *   <li>{@code content} - The main content body of the blog post</li>
 *   <li>{@code slug} - URL-friendly version of the title used in permalinks</li>
 *   <li>{@code author} - Reference to the user who authored this post (lazily loaded)</li>
 *   <li>{@code status} - The publication status of the post (defaults to DRAFT)</li>
 *   <li>{@code tags} - List of tags associated with the post for categorization (defaults to empty list)</li>
 *   <li>{@code publishedDate} - The date and time when the post was published (may be null if unpublished)</li>
 * </ul>
 */
@Document(collection = "posts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Post extends BaseAuditableEntity {
    @Id
    private String id;
    @TextIndexed(weight = 10)
    private String title;
    @TextIndexed(weight = 5)
    private String description;
    private String content;
    private String slug;
    @DBRef(lazy = true)
    private User author;
    @Builder.Default
    private PostStatus status = PostStatus.DRAFT;
    @Builder.Default
    private List<String> tags = List.of();
    private LocalDateTime publishedDate;
}

package io.github.mouhamethfadal.blogbackend.entities;

import io.github.mouhamethfadal.blogbackend.enums.PostStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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

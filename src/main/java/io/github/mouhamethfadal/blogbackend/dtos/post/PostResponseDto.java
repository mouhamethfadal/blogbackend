package io.github.mouhamethfadal.blogbackend.dtos.post;

import io.github.mouhamethfadal.blogbackend.dtos.user.AuthorDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponseDto {
    private String id;
    private String title;
    private String description;
    private String content;
    private String slug;
    private AuthorDto author;
    private List<String> tags;
    private LocalDateTime publishedDate;

}

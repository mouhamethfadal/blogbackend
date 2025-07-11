package io.github.mouhamethfadal.blogbackend.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostRequestDto {
    @NotBlank(message = "Title should not be blank")
    @Size(min = 5, max = 25, message = "Title should be between 5 and 25 characters")
    private String title;
    @NotBlank(message = "Description should not be blank")
    @Size(min = 25, max = 100, message = "Description should be between 25 and 100 characters")
    private String description;
    @NotBlank(message = "Content should not be blank")
    @Size(min = 25, message = "Content should be at least 25 characters")
    private String content;
    private List<String> tags;
}

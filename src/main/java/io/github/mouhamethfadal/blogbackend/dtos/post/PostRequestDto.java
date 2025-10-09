package io.github.mouhamethfadal.blogbackend.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

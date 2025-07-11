package io.github.mouhamethfadal.blogbackend.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private String username;
    private String email;
}

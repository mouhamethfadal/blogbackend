package io.github.mouhamethfadal.blogbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;
import io.github.mouhamethfadal.blogbackend.dtos.user.AuthorDto;
import io.github.mouhamethfadal.blogbackend.enums.PostStatus;
import io.github.mouhamethfadal.blogbackend.security.JwtAuthenticationFilter;
import io.github.mouhamethfadal.blogbackend.services.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        })
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PostService postService;
    @Autowired
    ObjectMapper objectMapper;

    AuthorDto authorDto = AuthorDto.builder()
            .username("john_doe")
            .email("john@example.com")
            .build();

    PostRequestDto postRequestDto = PostRequestDto.builder()
            .title("test")
            .description("test description")
            .content("#Hello")
            .tags(List.of("test1,test2"))
            .build();

    PostResponseDto postResponseDto = PostResponseDto.builder()
            .title("test")
            .description("test description")
            .content("#Hello")
            .slug("test")
            .author(authorDto)
            .status(PostStatus.DRAFT)
            .tags(List.of("test1,test2"))
            .build();

    @Test
    void createPost_WhenPostRequestDtoIsSent_ShouldCreatePost() throws Exception {
        // Arrange
        when(postService.createPost(postRequestDto)).thenReturn(postResponseDto);

        // Act
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(postRequestDto.getTitle()));
    }
}

package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;
import io.github.mouhamethfadal.blogbackend.entities.Post;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.mappers.PostMapper;
import io.github.mouhamethfadal.blogbackend.repositories.PostRepository;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostServiceImpl postService;

    final String username = "john_doe";
    final String postTitle = "My wonderful blog";
    final String postSlug = "my-wonderful-blog";

    User user = User.builder()
            .username(username)
            .build();
    Post post = Post.builder()
            .title(postTitle)
            .build();
    Post postWithSlug = Post
            .builder()
            .title(postTitle)
            .slug(postSlug)
            .author(user)
            .build();
    PostRequestDto postRequestDto = PostRequestDto.builder()
            .title(postTitle)
            .build();
    PostResponseDto postResponse = PostResponseDto.builder()
            .title(postTitle)
            .slug(postSlug)
            .build();

    private void mockAuthentication(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder) {
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
    }


    @Nested
    @DisplayName("createPost Test Cases")
    class CreatePostTests {
        @Test
        void createPost_WhenGivenAPost_ShouldCreateAndReturnPostResponseDto() {
            try(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
                // Arrange
                mockAuthentication(mockedSecurityContextHolder);
                when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
                when(postMapper.postRequestDtoToPost(postRequestDto)).thenReturn(post);
                when(postMapper.postToPostResponseDto(any(Post.class))).thenReturn(postResponse);
                when(postRepository.save(any(Post.class))).thenReturn(postWithSlug);

                // Act
                PostResponseDto postResponseDto = postService.createPost(postRequestDto);

                // Assert
                assertThat(postResponseDto).isNotNull();

                // verify
                verify(userRepository, times(1)).findByUsername(username);
                verify(postRepository, times(1)).save(any(Post.class));

            }
        }


    }
}

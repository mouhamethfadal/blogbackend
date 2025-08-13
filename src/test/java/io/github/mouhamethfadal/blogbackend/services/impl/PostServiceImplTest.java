package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.entities.Post;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
import io.github.mouhamethfadal.blogbackend.mappers.PostMapper;
import io.github.mouhamethfadal.blogbackend.repositories.PostRepository;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    final String testUsername = "john_doe";
    final String postTitle = "My wonderful blog";

    User testUser = User.builder()
            .username(testUsername)
            .build();
    Post testPost = Post.builder()
            .title(postTitle)
            .build();
    PostRequestDto testPostRequestDto = PostRequestDto.builder()
            .title(postTitle)
            .build();
    User user = User.builder()
            .username(testUsername)
            .build();

    @Nested
    @DisplayName("generateSlug Test Cases")
    class GenerateSlugTests {
        private Method generateSlug;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            generateSlug = PostServiceImpl.class.getDeclaredMethod("generateSlug", String.class);
            generateSlug.setAccessible(true);
        }

        @Test
        void generateSlug_ShouldReturnCorrectSlug() throws InvocationTargetException, IllegalAccessException {
            String simpleTitle = "My wonderful blog";
            String complexTitle = "---my2 wonderful*#23 blog --^ù$$^post---";
            String expectedSlugForSimpleTitle = "my-wonderful-blog";
            String expectedSlugForComplexTitle = "my2-wonderful-23-blog-post";

            // Act
            String generatedSlugForSimpleTitle = (String) generateSlug.invoke(postService, simpleTitle);
            String generatedSlugForComplexTitle = (String) generateSlug.invoke(postService, complexTitle);

            // Assert
            assertThat(generatedSlugForSimpleTitle).isEqualTo(expectedSlugForSimpleTitle);
            assertThat(generatedSlugForComplexTitle).isEqualTo(expectedSlugForComplexTitle);
        }

    }

    @Nested
    @DisplayName("getLoggedInUser Test Cases")
    class GetLoggedInUserTests {
        private Method getLoggedInUser;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            getLoggedInUser = PostServiceImpl.class.getDeclaredMethod("getLoggedInUser");
            getLoggedInUser.setAccessible(true);
        }

        private void mockAuthentication(MockedStatic<SecurityContextHolder> mockedSecurityContextHolder) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(testUsername);
        }

        @Test
        void getLoggedInUser_WhenUserIsLoggedInAndFound_ShouldReturnUser() throws InvocationTargetException, IllegalAccessException {
            // Arrange
            try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
                mockAuthentication(mockedSecurityContextHolder);
                when(userRepository.findByUsername(testUsername)).thenReturn(Optional.of(testUser));
                // Act
                User loggedInUser = (User) getLoggedInUser.invoke(postService);

                // Assert
                assertThat(loggedInUser).isEqualTo(testUser);
            }
        }


        @Test
        void getLoggedInUser_WhenUserNotFound_ShouldThrowException() throws UserNotFoundException {
            // Arrange
            try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
                mockAuthentication(mockedSecurityContextHolder);
                when(userRepository.findByUsername(testUsername)).thenThrow(new UserNotFoundException(testUsername));
                // Act
               assertThatThrownBy(() -> getLoggedInUser.invoke(postService))
                       .isInstanceOf(InvocationTargetException.class)
                       .hasCauseInstanceOf(UserNotFoundException.class)
                       .extracting(Throwable::getCause)
                       .extracting(Throwable::getMessage)
                       .asString()
                       .contains(testUsername);

            }


        }
    }

    @Nested
    @DisplayName("setPostSlug Test Cases")
    class SetPostSlugTests {
        private Method setPostSlug;
        @BeforeEach
        void setUp() throws NoSuchMethodException {
            setPostSlug = PostServiceImpl.class.getDeclaredMethod("setPostSlug", Post.class);
            setPostSlug.setAccessible(true);
        }

        @Test
        void setPostSlug_WhenGivenAPostWithTitle_ShouldSetPostSlug() throws InvocationTargetException, IllegalAccessException {

            // Verify before appending slug
            assertThat(testPost.getSlug()).isNull();

            // Act
            Post postWithSlug = (Post) setPostSlug.invoke(postService, testPost);

            // Assert
            assertThat(postWithSlug.getSlug()).isEqualTo("my-wonderful-blog");
        }
    }

    @Nested
    @DisplayName("setPostAuthor Test Cases")
    class SetPostAuthorTests {
        private Method setPostAuthor;
        @BeforeEach
        void setUp() throws NoSuchMethodException {
            setPostAuthor = PostServiceImpl.class.getDeclaredMethod("setPostAuthor", PostRequestDto.class, User.class);
            setPostAuthor.setAccessible(true);
        }

        @Test
        void setPostAuthor_WhenGivenAPostRequestDtoAndUser_ShouldSetPostAuthor() throws InvocationTargetException, IllegalAccessException {

            // Arrange
            when(postMapper.postRequestDtoToPost(testPostRequestDto)).thenReturn(testPost);

            // Verify before setting author
            assertThat(postMapper.postRequestDtoToPost(testPostRequestDto).getAuthor()).isNull();

            // Act
            Post postWithAuthor = (Post) setPostAuthor.invoke(postService, testPostRequestDto, user);

            // Assert
            assertThat(postWithAuthor.getAuthor()).isEqualTo(testUser);
        }

    }

    @Nested
    @DisplayName("savePost Test Cases")
    class SavePostTests {
        private Method savePost;
        @BeforeEach
        void setUp() throws NoSuchMethodException {
            savePost = PostServiceImpl.class.getDeclaredMethod("savePost", Post.class);
            savePost.setAccessible(true);
        }

        @Test
        void savePost_WhenGivenAPost_ShouldSavePost() throws InvocationTargetException, IllegalAccessException {
            // Act
            savePost.invoke(postService, testPost);

            // Verify
            verify(postRepository, times(1)).save(testPost);
        }
    }
}

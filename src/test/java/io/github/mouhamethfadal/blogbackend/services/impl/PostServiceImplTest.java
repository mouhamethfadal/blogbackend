package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postService;

    final String testUsername = "john_doe";

    User testUser = User.builder()
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
    class getLoggedInUserTests {
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
}

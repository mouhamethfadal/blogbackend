package io.github.mouhamethfadal.blogbackend.security;

import io.github.mouhamethfadal.blogbackend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private UserDetails userDetails;
    @Mock
    SecurityContext securityContext;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Method getJwtFromRequest;

    private static final String TOKEN = "F#+@23fjjfkk##...";
    private static final String BEARER_PREFIX = "Bearer ";
    private final String authorizationPrefix = "Authorization";

    @Nested
    @DisplayName("doFilterInternal Test Cases")
    class doFilterInternalTests {
        @BeforeEach
        void setup() {
            SecurityContextHolder.setContext(securityContext);
        }

        @Test
        void doFilterInternal_WhenJwtIsValid_ShouldAuthenticateUser() throws ServletException, IOException {
            final String username = "testUser";
            // Arrange
            when(request.getHeader(authorizationPrefix)).thenReturn(BEARER_PREFIX + TOKEN);
            when(jwtService.validateToken(TOKEN)).thenReturn(true);
            when(jwtService.getUsernameFromToken(TOKEN)).thenReturn(username);
            when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

            doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .when(userDetails).getAuthorities();

            // Act
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            //Verify
            ArgumentCaptor<UsernamePasswordAuthenticationToken> authentication = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
            verify(securityContext).setAuthentication(authentication.capture());

            // Assert
            UsernamePasswordAuthenticationToken authenticationToken = authentication.getValue();
            assertThat(authenticationToken.getPrincipal()).isEqualTo(userDetails);
            assertThat(authenticationToken.getDetails()).isNotNull();
            assertThat(authenticationToken.getDetails()).isInstanceOf(WebAuthenticationDetails.class);
            assertThat(authenticationToken.getAuthorities()).hasSize(1).isEqualTo(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            // Verify
            verify(filterChain).doFilter(request, response);

        }

        @ParameterizedTest(name = "#{index} - Test with token: {0}")
        @MethodSource("provideNonTextualToken")
        void doFilterInternal_WhenTokenDoesNotContainText_ShouldContinueFilterChain(ArgumentsAccessor args) throws Exception {
            // Arrange
            when(request.getHeader(authorizationPrefix)).thenReturn(BEARER_PREFIX + args.getString(1));

            // Act
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Verify
            verify(jwtService, never()).validateToken(args.getString(1));
            verify(jwtService, never()).getUsernameFromToken(anyString());
            verify(userDetailsService, never()).loadUserByUsername(anyString());
            verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
            verify(filterChain).doFilter(request, response);

        }

        static Stream<Arguments> provideNonTextualToken() {
            return Stream.of(
                    Arguments.of("Empty token", ""),
                    Arguments.of("Blank token", " "),
                    Arguments.of("Tab character (\\t)", "\t"),
                    Arguments.of("Newline character (\\n) ", "\n"),
                    Arguments.of("Tab and Newline character (\\t\\n)", "\n\t")
            );
        }

        @Test
        void doFilterInternal_WhenTokenIsInvalidShouldContinueFilterChain() throws Exception {
            // Arrange
            when(request.getHeader(authorizationPrefix)).thenReturn(BEARER_PREFIX + TOKEN);
            when(jwtService.validateToken(TOKEN)).thenReturn(false);

            // Act
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Verify
            verify(jwtService, times(1)).validateToken(TOKEN);
            verify(jwtService, never()).getUsernameFromToken(anyString());
            verify(userDetailsService, never()).loadUserByUsername(anyString());
            verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
            verify(filterChain).doFilter(request, response);
        }

    }

    @Nested
    @DisplayName("getJwtFromRequest Test Cases")
    class getJwtFromRequestTests {
        @BeforeEach
        void setUp() throws NoSuchMethodException {
            getJwtFromRequest = JwtAuthenticationFilter.class.getDeclaredMethod("getJwtFromRequest", HttpServletRequest.class);
            getJwtFromRequest.setAccessible(true);
        }

        @ParameterizedTest(name = "#{index} - Test with token: {1}")
        @MethodSource("provideInvalidToken")
        void getJwtRequest_ShouldReturnCorrectValue(ArgumentsAccessor args) throws Exception {
            // Arrange
            when(request.getHeader(authorizationPrefix)).thenReturn(args.getString(1));

            // Act
            String result = (String) getJwtFromRequest.invoke(jwtAuthenticationFilter, request);

            // Assert
            assertThat(result).isEqualTo(args.getString(2));
        }

        private static Stream<Arguments> provideInvalidToken() {
            return Stream.of(
                    Arguments.of("Token is Bearer", BEARER_PREFIX  + TOKEN, TOKEN),
                    Arguments.of("Token is empty", "", null),
                    Arguments.of("Token is not Bearer", "Basic " + TOKEN, null)
            );
        }
    }

}

package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.request.LoginRequest;
import io.github.mouhamethfadal.blogbackend.dtos.request.RegisterRequest;
import io.github.mouhamethfadal.blogbackend.dtos.response.AuthResponse;
import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.EmailAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.exceptions.UsernameAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import io.github.mouhamethfadal.blogbackend.services.JwtService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock(strictness = Mock.Strictness.LENIENT)
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock(strictness = Mock.Strictness.LENIENT)
    private JwtService jwtService;
    @InjectMocks
    private AuthServiceImpl authService;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private final String  jwt = "eYkfjfj~#..";

    @BeforeEach
    void setUp() {
        final String testUser = "testUser";
        final String mail = "testUser@exampe.com";
        final String password = "pass";
        registerRequest = RegisterRequest.builder()
                .username(testUser)
                .email(mail)
                .password(password)
                .build();

        loginRequest = LoginRequest.builder()
                .username(testUser)
                .password(password)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(any(Authentication.class))).thenReturn(jwt);
    }

    @Nested
    @DisplayName("validateNewUSer Test Cases")
    class validateNewUSerTests {
        private Method validateNewUser;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            validateNewUser = AuthServiceImpl.class.getDeclaredMethod("validateNewUser", RegisterRequest.class);
            validateNewUser.setAccessible(true);
        }

        @Test
        void validateNewUser_WhenUsernameAlreadyExists_ShouldThrowException() {
            // Arrange
            when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

            // Act and Assert
            assertThatThrownBy(() -> validateNewUser.invoke(authService, registerRequest))
                    .isInstanceOf(InvocationTargetException.class)
                    .hasCauseInstanceOf(UsernameAlreadyExistsException.class)
                    .extracting(Throwable::getCause)
                    .extracting(Throwable::getMessage)
                    .isEqualTo("Username already exists: "+ registerRequest.getUsername());

            verify(userRepository, never()).existsByEmail(registerRequest.getEmail());

        }

        @Test
        void validateNewUser_WhenEmailAlreadyExists_ShouldThrowException() {
            // Arrange
            when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
            when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

            // Act and Assert
            assertThatThrownBy(() -> validateNewUser.invoke(authService, registerRequest))
                    .isInstanceOf(InvocationTargetException.class)
                    .hasCauseInstanceOf(EmailAlreadyExistsException.class)
                    .extracting(Throwable::getCause)
                    .extracting(Throwable::getMessage)
                    .isEqualTo("Email already exists: " + registerRequest.getEmail());
        }

        @Test
        void validateNewUser_WhenUsernameAndEmailDontExist_ShouldNotThrowException() {
            // Arrange
            when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
            when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);

            // Act and Assert
            assertThatCode(() -> validateNewUser.invoke(authService, registerRequest))
                    .doesNotThrowAnyException();
        }

    }

    @Nested
    @DisplayName("createUser Test Cases")
    class createUserTests {
        private Method createUser;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            createUser = AuthServiceImpl.class.getDeclaredMethod("createUser", RegisterRequest.class);
            createUser.setAccessible(true);
        }
        @ParameterizedTest
        @MethodSource("provideRoles")
        void createUser_ShouldCreateUser_Appropriately(Long count, Set<Role> expectedRoles) throws InvocationTargetException, IllegalAccessException {
            // Arrange
            when(userRepository.count()).thenReturn(count);
            when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("p#h&");

            // Act
            User user = (User) createUser.invoke(authService, registerRequest);

            // Assert
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo(registerRequest.getUsername());
            assertThat(user.getEmail()).isEqualTo(registerRequest.getEmail());
            assertThat(user.getRoles())
                    .hasSize(expectedRoles.size())
                    .containsExactlyInAnyOrderElementsOf(expectedRoles);

            verify(userRepository, times(1)).save(user);

        }

        static Stream<Arguments> provideRoles() {
            return Stream.of(
                    Arguments.of(0L, Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)),
                    Arguments.of(1L, Set.of(Role.ROLE_USER))
            );
        }
    }

    @Nested
    @DisplayName("buildAuthResponse Test Cases")
    class buildAuthResponseTests {
        private Method buildAuthResponse;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            buildAuthResponse = AuthServiceImpl.class.getDeclaredMethod("buildAuthResponse", String.class, String.class);
            buildAuthResponse.setAccessible(true);
        }

        @Test
        void buildAuthResponse_WhenGivenJwtAndUsername_ShouldReturnAuthResponse() throws InvocationTargetException, IllegalAccessException {
            // Act
            final String username = "testUser";
            AuthResponse authResponse = (AuthResponse) buildAuthResponse.invoke(authService, jwt, username);

            // Assert
            assertThat(authResponse).isNotNull();
            assertThat(authResponse.getToken()).isEqualTo(jwt);
            assertThat(authResponse.getUsername()).isEqualTo(username);
        }
    }

    @Nested
    @DisplayName("register user Test Cases")
    class registerTests {
        @BeforeEach
        void setUp() {
            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
        }

        @ParameterizedTest(name = "#{index} - Test with user: {0}")
        @MethodSource("provideUserType")
        void register_WhenEnrollingUser_ShouldReturnCorrectUserType(ArgumentsAccessor args) {
            // Arrange
            when(userRepository.count()).thenReturn(args.getLong(1));

            // Act
            AuthResponse authResponse = authService.register(registerRequest);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository, times(1)).save(userCaptor.capture());

            User user = userCaptor.getValue();

            @SuppressWarnings("unchecked")
            Set<Role> expectedRoles = (Set<Role>) args.get(2);

            // Assert
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo(registerRequest.getUsername());
            assertThat(user.getEmail()).isEqualTo(registerRequest.getEmail());
            assertThat(user.getRoles())
                    .hasSize(expectedRoles.size())
                    .containsExactlyInAnyOrderElementsOf(expectedRoles);

            assertThat(authResponse.getToken()).isEqualTo(jwt);
            assertThat(authResponse.getUsername()).isEqualTo(registerRequest.getUsername());

        }

        static Stream<Arguments> provideUserType() {
            return Stream.of(
                    Arguments.of("Admin user", 0L, Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)),
                    Arguments.of("Simple user", 3L, Set.of(Role.ROLE_USER))
            );
        }
    }

    @Nested
    @DisplayName("login user Test Cases")
    class loginTests {
        @Test
        void login_WhenUserProvideValidCredentials_ShouldReturnToken() {
            // Act
            AuthResponse authResponse = authService.login(loginRequest);

            // Assert
            assertThat(authResponse).isNotNull();
            assertThat(authResponse.getToken()).isEqualTo(jwt);
            assertThat(authResponse.getUsername()).isEqualTo(loginRequest.getUsername());
        }

        @Test
        void login_WhenUserProvideInvalidCredentials_ShouldThrowException() {
            // Arrange
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Bad credentials"));

            // Assert
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Bad credentials");
        }
    }
}

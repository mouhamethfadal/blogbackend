package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.response.AuthResponse;
import io.github.mouhamethfadal.blogbackend.dtos.request.LoginRequest;
import io.github.mouhamethfadal.blogbackend.dtos.request.RegisterRequest;
import io.github.mouhamethfadal.blogbackend.entities.Role;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.EmailAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.exceptions.UsernameAlreadyExistsException;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import io.github.mouhamethfadal.blogbackend.services.AuthService;
import io.github.mouhamethfadal.blogbackend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        validateNewUser(request);

        User user = createUser(request);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateToken(authentication);

        return buildAuthResponse(jwt, user.getUsername());

    }

    private void validateNewUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
    }

    private User createUser(RegisterRequest request) {
        boolean isFirstUser = userRepository.count() == 0;

        Supplier<Set<Role>> roleSupplier = isFirstUser
                ? () -> Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)
                : () -> Set.of(Role.ROLE_USER);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roleSupplier.get())
                .enabled(isFirstUser)
                .build();

        userRepository.save(user);

        return user;
    }


    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateToken(authentication);

        return buildAuthResponse(jwt, request.getUsername());
    }

    private AuthResponse buildAuthResponse(String jwt, String username) {
        return AuthResponse.builder()
                .token(jwt)
                .username(username)
                .build();
    }
}

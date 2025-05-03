package io.github.mouhamethfadal.blogbackend.services;


import io.github.mouhamethfadal.blogbackend.dtos.response.AuthResponse;
import io.github.mouhamethfadal.blogbackend.dtos.request.LoginRequest;
import io.github.mouhamethfadal.blogbackend.dtos.request.RegisterRequest;

public interface AuthService {

    /**
     * This method registers a new user
     * @param request containing user's username, password and email.
     * @return AuthResponse containing user's username and token.
     * @see RegisterRequest
     * @see AuthResponse
     */
    AuthResponse register(RegisterRequest request);

    /**
     * This method logs the user in
     * @param request containing user's username and password.
     * @return AuthResponse containing user's username and token.
     * @see LoginRequest
     * @see AuthResponse
     */
    AuthResponse login(LoginRequest request);
}

package io.github.mouhamethfadal.blogbackend.controllers;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User management", description = "Find, update, delete users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Find all users",
            description = "Find all registered users"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "username": "john_doe",
                                                "email": "john_doe@example.com",
                                                "roles": ["ROLE_ADMIN", "ROLE_USER"]
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    ref = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "Forbidden"
            )
    })
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(
            summary = "Find a user by his username",
            description = "Find a registered user by his username"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                              {
                                                "username": "john_doe",
                                                "email": "john_doe@example.com",
                                                "roles": ["ROLE_ADMIN", "ROLE_USER"]
                                              }
                                            
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    ref = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "403",
                    ref = "Forbidden"
            ),
            @ApiResponse(
                    responseCode = "404",
                    ref = "NotFound"
            )
    })
    @GetMapping("/{username}")
    public UserDto getUserByUsername(
            @Parameter(description = "Username of the user to find", example = "john_doe", required = true) @PathVariable String username
    ) {
        return userService.findUserByUsername(username);
    }
}
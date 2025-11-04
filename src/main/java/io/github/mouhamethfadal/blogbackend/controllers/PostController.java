package io.github.mouhamethfadal.blogbackend.controllers;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;
import io.github.mouhamethfadal.blogbackend.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Post management", description = "Create, read, update, and delete blog posts")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "Create a new blog post",
            description = "Create a new blog post with markdown content"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "507f1f77bcf86cd799439011",
                                              "title": "Getting Started with Spring Boot",
                                              "description": "A comprehensive guide to building REST APIs with Spring Boot",
                                              "content": "# Introduction\\n\\nSpring Boot makes it easy to create stand-alone...",
                                              "slug": "getting-started-with-spring-boot",
                                              "status": "DRAFT",
                                              "tags": ["spring-boot", "java", "backend"],
                                              "createdAt": "2025-10-07T10:30:00",
                                              "updatedAt": "2025-10-07T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "Title should not be blank",
                                              "description": "Description should not be blank",
                                              "content": "Content should not be blank"
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
            )
    })
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data with markdown content",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PostRequestDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "title": "Getting Started with Spring Boot",
                                              "description": "A comprehensive guide to building REST APIs with Spring Boot",
                                              "content": "# Introduction\\n\\nSpring Boot makes it easy to create stand-alone, production-grade Spring based Applications.\\n\\n## Features\\n\\n- Auto-configuration\\n- Standalone\\n- Production-ready",
                                              "tags": ["spring-boot", "java", "backend"]
                                            }
                                            """
                            )
                    )
            )
            @RequestBody @Valid PostRequestDto postRequestDto) {
        PostResponseDto createdPost = postService.createPost(postRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @Operation(
            summary = "Get all blog posts",
            description = "Retrieve a list of all blog posts"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Posts retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": "507f1f77bcf86cd799439011",
                                                "title": "Getting Started with Spring Boot",
                                                "description": "A comprehensive guide to building REST APIs with Spring Boot",
                                                "content": "# Introduction\\n\\nSpring Boot makes it easy to create stand-alone...",
                                                "slug": "getting-started-with-spring-boot",
                                                "status": "PUBLISHED",
                                                "tags": ["spring-boot", "java", "backend"],
                                                "createdAt": "2025-10-07T10:30:00",
                                                "updatedAt": "2025-10-07T10:30:00"
                                              },
                                              {
                                                "id": "507f1f77bcf86cd799439012",
                                                "title": "Introduction to MongoDB",
                                                "description": "Learn the basics of MongoDB NoSQL database",
                                                "content": "# MongoDB Basics\\n\\nMongoDB is a document-oriented database...",
                                                "slug": "introduction-to-mongodb",
                                                "status": "PUBLISHED",
                                                "tags": ["mongodb", "database", "nosql"],
                                                "createdAt": "2025-10-08T14:20:00",
                                                "updatedAt": "2025-10-08T14:20:00"
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
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }
}

package io.github.mouhamethfadal.blogbackend.services;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;

import java.util.List;

public interface PostService {
    /**
     *
     * @param postRequestDto the post object sent by the author
     * @return PostResponseDto a post object enriched with data
     */
    PostResponseDto createPost(PostRequestDto postRequestDto);

    /**
     * Get all posts from the database
     * @return a list of PostResponseDto objects
     */
    List<PostResponseDto> getAllPosts();
}

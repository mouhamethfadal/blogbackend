package io.github.mouhamethfadal.blogbackend.services;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;

public interface PostService {
    /**
     *
     * @param postRequestDto the post object sent by the author
     * @return PostResponseDto a post object enriched with data
     */
    PostResponseDto createPost(PostRequestDto postRequestDto);
}

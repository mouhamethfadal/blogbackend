package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;
import io.github.mouhamethfadal.blogbackend.entities.Post;
import io.github.mouhamethfadal.blogbackend.entities.User;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
import io.github.mouhamethfadal.blogbackend.mappers.PostMapper;
import io.github.mouhamethfadal.blogbackend.repositories.PostRepository;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import io.github.mouhamethfadal.blogbackend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        User user = getLoggedInUser();

        Post post = setPostAuthor(postRequestDto, user);

        return savePost(setPostSlug(post));
    }

    private PostResponseDto savePost(Post post) {
        Post savedPost = postRepository.save(post);

        return postMapper.postToPostResponseDto(savedPost);
    }

    private Post setPostAuthor(PostRequestDto postRequestDto, User user) {
        Post post = postMapper.postRequestDtoToPost(postRequestDto);

        post.setAuthor(user);
        return post;
    }

    private User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow( () -> new UserNotFoundException(username) );
    }

    private Post setPostSlug(Post post) {
        String postSlug = generateSlug(post.getTitle());
        post.setSlug(postSlug);

        return post;
    }

    private String generateSlug(String postTitle){
        return postTitle.toLowerCase()
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("(^-)|(-$)", "");
    }
}

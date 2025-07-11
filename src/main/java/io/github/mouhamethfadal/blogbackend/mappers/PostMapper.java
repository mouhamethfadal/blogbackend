package io.github.mouhamethfadal.blogbackend.mappers;

import io.github.mouhamethfadal.blogbackend.dtos.post.PostRequestDto;
import io.github.mouhamethfadal.blogbackend.dtos.post.PostResponseDto;
import io.github.mouhamethfadal.blogbackend.entities.Post;
import lombok.Generated;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
@Generated
public interface PostMapper {
    PostResponseDto postToPostResponseDto(Post post);
    Post postRequestDtoToPost(PostRequestDto postRequestDto);
}

package io.github.mouhamethfadal.blogbackend.mappers;

import io.github.mouhamethfadal.blogbackend.dtos.user.AuthorDto;
import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.User;
import lombok.Generated;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@Generated
public interface UserMapper {
    UserDto userToUserDto(User user);
    AuthorDto userToAuthorDto(User user);
}

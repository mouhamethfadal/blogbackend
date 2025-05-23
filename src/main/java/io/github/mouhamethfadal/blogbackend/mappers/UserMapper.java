package io.github.mouhamethfadal.blogbackend.mappers;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles")
    UserDto userToUserDto(User user);
}

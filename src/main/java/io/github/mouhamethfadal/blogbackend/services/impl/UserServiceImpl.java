package io.github.mouhamethfadal.blogbackend.services.impl;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.exceptions.UserNotFoundException;
import io.github.mouhamethfadal.blogbackend.mappers.UserMapper;
import io.github.mouhamethfadal.blogbackend.repositories.UserRepository;
import io.github.mouhamethfadal.blogbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .toList();
    }
    
    @Override
    public UserDto findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::userToUserDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

}

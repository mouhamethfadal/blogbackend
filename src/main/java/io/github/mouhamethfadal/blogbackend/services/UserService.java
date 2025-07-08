package io.github.mouhamethfadal.blogbackend.services;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;

import java.util.List;

public interface UserService {
  /**
   * This method returns all users registered in the app.
   * @return all users
   */
  List<UserDto> findAllUsers();

  /**
   * This method finds a registered user by its username
   *
   * @param username the username
   * @return a user with the matching username
   */
  UserDto findUserByUsername(String username);

  /**
   *
   * @param username the user's username to be enabled
   * @return the enabled user
   */
  UserDto enableUser(String username);
}

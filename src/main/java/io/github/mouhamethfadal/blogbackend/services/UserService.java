package io.github.mouhamethfadal.blogbackend.services;

import io.github.mouhamethfadal.blogbackend.dtos.user.UserDto;
import io.github.mouhamethfadal.blogbackend.entities.User;

import java.util.List;

/**
 * UserService provides methods to handle operations related to User entities.
 */
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

  /**
   * Activates a user by setting their enabled status to true.
   * @param user the user entity to be activated
   * @return the updated user entity after activation and persistence
   */
  User activateUser(User user);
}

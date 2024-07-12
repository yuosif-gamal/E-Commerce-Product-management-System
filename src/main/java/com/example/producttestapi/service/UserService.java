package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.User;

import java.util.List;

public interface UserService {

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto deleteUserById(Long userId);
    User findUserByEmail(String email);
}

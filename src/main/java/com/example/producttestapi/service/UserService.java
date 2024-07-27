package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.User;

import java.util.List;

public interface UserService {
    void register(UserDto request);

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(int userId);

    UserDto deleteUserById(int userId);
    User findUserByEmail(String email);
}

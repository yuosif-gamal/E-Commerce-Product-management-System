package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entity.User;

import java.util.List;

public interface UserService {
    void register(UserDto request);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto deleteUserById(Long userId);

    User currentUser();



    UserDto changeRole(Long id, String myRole);
}

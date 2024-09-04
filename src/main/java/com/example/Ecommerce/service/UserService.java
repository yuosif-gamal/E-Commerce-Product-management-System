package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.UserDto;
import com.example.Ecommerce.entity.User;

import java.util.List;

public interface UserService {
    void register(UserDto request);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto deleteUserById(Long userId);

    User currentUser();


    UserDto changeRole(Long id, String myRole);

    User changeSubscribeStatus(Long id);
}

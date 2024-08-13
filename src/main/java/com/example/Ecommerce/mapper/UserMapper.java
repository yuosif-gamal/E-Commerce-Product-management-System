package com.example.Ecommerce.mapper;

import com.example.Ecommerce.dto.UserDto;
import com.example.Ecommerce.entity.User;

public class UserMapper {

    public static UserDto convertEntityToDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public static User convertDtoToEntity(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }
}

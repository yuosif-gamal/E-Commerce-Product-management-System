package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.UserMapper;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final RoleService roleService;

    public UserServiceImpl(UserRepo userRepo, RoleService roleService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
    }

    @Override
    public void createUser(UserDto userDto) {
        User user = UserMapper.convertDtoToEntity(userDto);
        userRepo.save(user);
    }



    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepo.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User user:userList) {
            UserDto userDto = UserMapper.convertEntityToDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepo.getUserById(userId);
        return UserMapper.convertEntityToDto(user);
    }

    @Override
    public UserDto deleteUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepo.deleteById(userId);
        return UserMapper.convertEntityToDto(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepo.findAllByEmail(email);
    }
}

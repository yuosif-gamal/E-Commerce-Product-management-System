package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entity.Role;
import com.example.producttestapi.exception.DuplicateResourceException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.UserMapper;
import com.example.producttestapi.entity.User;
import com.example.producttestapi.repository.UserRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class    UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    public UserServiceImpl(UserRepo userRepo, RoleService roleService, PasswordEncoder passwordEncoder ) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder= passwordEncoder;
    }

    @Override
    public void register(UserDto request) {
        User existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new DuplicateResourceException("Email is connected to another account.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserMapper.convertDtoToEntity(request);
        Role userRole = roleService.findRoleByName("USER");
        user.addRole(userRole);
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
    public UserDto getUserById(int userId) {
        User user = userRepo.getUserById(userId);
        return UserMapper.convertEntityToDto(user);
    }

    @Override
    public UserDto deleteUserById(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepo.deleteById(userId);
        return UserMapper.convertEntityToDto(user);
    }




    @Override
    @Cacheable(value = "user")
    public User currentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        User user = userRepo.findByEmail(email);
        return user;
    }

    @Override
    public UserDto changeRole(int id, String myRole) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No user found with id: " + id));
        Role role = roleService.findRoleByName(myRole);
        user.getRoles().clear();
        user.addRole(role);
        userRepo.save(user);
        return UserMapper.convertEntityToDto(user);
    }

}

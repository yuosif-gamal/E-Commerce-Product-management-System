package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.DuplicateResourceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserDto request) {
        User u = userService.findUserByEmail(request.getEmail());
        if(u != null){
            throw new DuplicateResourceException("Email is connected to another account.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.createUser(request);
    }
}

package com.example.producttestapi.service;

import com.example.producttestapi.model.RegistrationRequest;
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
    public void register(RegistrationRequest request) {
        if(userService.UserExistsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email is connected to another account.");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.createUser(request);
    }
}

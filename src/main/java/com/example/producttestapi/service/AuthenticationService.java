package com.example.producttestapi.service;

import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.model.RegistrationRequest;

public interface AuthenticationService {
    void register(UserDto request);
}

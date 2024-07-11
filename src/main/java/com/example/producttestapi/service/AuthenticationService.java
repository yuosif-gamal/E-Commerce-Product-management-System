package com.example.producttestapi.service;

import com.example.producttestapi.entities.RegistrationRequest;

public interface AuthenticationService {
    public void register(RegistrationRequest request);
    // Todo login
}

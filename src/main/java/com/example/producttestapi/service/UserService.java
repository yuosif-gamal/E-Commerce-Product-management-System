package com.example.producttestapi.service;

import com.example.producttestapi.entities.RegistrationRequest;
import com.example.producttestapi.entities.User;

public interface UserService {
    public void createUser(RegistrationRequest request);
    public User findUserByEmail(String email);
    public boolean UserExistsByEmail(String email);
}

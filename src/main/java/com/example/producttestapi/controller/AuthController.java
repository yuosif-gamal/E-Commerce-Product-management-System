package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.model.RegistrationRequest;
import com.example.producttestapi.service.AuthenticationService;
import com.example.producttestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserDto request){
        authenticationService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
// to do login !
}

package com.example.producttestapi.controller;

import com.example.producttestapi.entities.RegistrationRequest;
import com.example.producttestapi.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody RegistrationRequest request){
        authenticationService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

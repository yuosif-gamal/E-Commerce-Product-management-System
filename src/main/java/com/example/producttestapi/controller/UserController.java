package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.service.AuthenticationService;
import com.example.producttestapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Validated
@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController( UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> registerNewUser(@Valid  @RequestBody UserDto request){
        userService.register(request);
        return  ResponseEntity.ok(new SuccessResponse("User created .. " , true , request ,  HttpStatus.OK.value()));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<SuccessResponse> getUserById(@PathVariable("id") int userId) {
        UserDto savedUser = userService.getUserById(userId);
        return ResponseEntity.ok(new SuccessResponse("User saved successfully", true, savedUser, HttpStatus.OK.value()));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<SuccessResponse> deleteUserById(@PathVariable("id") int userId) {
        UserDto deletedUser = userService.deleteUserById(userId);
        return ResponseEntity.ok(new SuccessResponse("User deleted successfully", true, deletedUser, HttpStatus.OK.value()));
    }
}

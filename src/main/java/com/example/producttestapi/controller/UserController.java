package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.service.AuthenticationService;
import com.example.producttestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
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

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        UserDto savedUser = userService.getUserById(userId);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<SuccessResponse> deleteUserById(@PathVariable("id") Long userId) {
        UserDto deletedUser = userService.deleteUserById(userId);
        return ResponseEntity.ok(new SuccessResponse("User deleted successfully", true, deletedUser, HttpStatus.OK.value()));
    }
}

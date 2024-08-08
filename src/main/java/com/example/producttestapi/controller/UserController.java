package com.example.producttestapi.controller;

import com.example.producttestapi.dto.SuccessResponse;
import com.example.producttestapi.dto.UserDto;
import com.example.producttestapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Find All Users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/register")
    @Operation(summary = "Register New User", description = "Register a new user with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided")
    })
    public ResponseEntity<SuccessResponse> registerNewUser(@Valid @RequestBody UserDto request) {
        userService.register(request);
        return ResponseEntity.ok(new SuccessResponse("User created .. ", true, request, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User By ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> getUserById(@PathVariable("id") int userId) {
        UserDto savedUser = userService.getUserById(userId);
        return ResponseEntity.ok(new SuccessResponse("User saved successfully", true, savedUser, HttpStatus.OK.value()));
    }

    @PutMapping("/make-admin/{id}")
    @Operation(summary = "Change User Role to Admin", description = "Change the role of a user to Admin")
    @ApiResponse(responseCode = "200", description = "User role changed to Admin successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToAdmin(@PathVariable("id") int id) {
        UserDto user = userService.changeRole(id, "ADMIN");
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became Admin successfully", true, user, HttpStatus.OK.value()));
    }

    @PutMapping("/make-manager/{id}")
    @Operation(summary = "Change User Role to Manager", description = "Change the role of a user to Manager")
    @ApiResponse(responseCode = "200", description = "User role changed to Manager successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToManager(@PathVariable("id") int id) {
        UserDto user = userService.changeRole(id, "MANAGER");
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became Manager successfully", true, user, HttpStatus.OK.value()));
    }

    @PutMapping("/make-user/{id}")
    @Operation(summary = "Change User Role to User", description = "Change the role of a user to User")
    @ApiResponse(responseCode = "200", description = "User role changed to User successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToUser(@PathVariable("id") int id) {
        UserDto user = userService.changeRole(id, "USER");
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became User successfully", true, user, HttpStatus.OK.value()));
    }
}

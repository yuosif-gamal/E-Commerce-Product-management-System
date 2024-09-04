package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.SuccessResponse;
import com.example.Ecommerce.dto.UserDto;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @Operation(summary = "Find All Users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        LOGGER.info("Received request to retrieve all users");
        List<UserDto> userList = userService.getAllUsers();
        LOGGER.info("Returning list of {} users", userList.size());
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/register")
    @Operation(summary = "Register New User", description = "Register a new user with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided")
    })
    public ResponseEntity<SuccessResponse> registerNewUser(@Valid @RequestBody UserDto request) {
        LOGGER.info("Received request to register new user with details: {}", request);
        userService.register(request);
        LOGGER.info("User registered successfully with details: {}", request);
        return ResponseEntity.ok(new SuccessResponse("User created .. ", request, HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User By ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> getUserById(@PathVariable("id") Long userId) {
        LOGGER.info("Received request to retrieve user with ID {}", userId);
        UserDto savedUser = userService.getUserById(userId);
        LOGGER.info("Returning details for user with ID {}", userId);
        return ResponseEntity.ok(new SuccessResponse("User saved successfully", savedUser, HttpStatus.OK.value()));
    }

    @PutMapping("/make-admin/{id}")
    @Operation(summary = "Change User Role to Admin", description = "Change the role of a user to Admin")
    @ApiResponse(responseCode = "200", description = "User role changed to Admin successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToAdmin(@PathVariable("id") Long id) {
        LOGGER.info("Received request to change user with ID {} to Admin", id);
        UserDto user = userService.changeRole(id, "ADMIN");
        LOGGER.info("User with ID {} changed to Admin successfully", id);
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became Admin successfully", user, HttpStatus.OK.value()));
    }

    @PutMapping("/make-manager/{id}")
    @Operation(summary = "Change User Role to Manager", description = "Change the role of a user to Manager")
    @ApiResponse(responseCode = "200", description = "User role changed to Manager successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToManager(@PathVariable("id") Long id) {
        LOGGER.info("Received request to change user with ID {} to Manager", id);
        UserDto user = userService.changeRole(id, "MANAGER");
        LOGGER.info("User with ID {} changed to Manager successfully", id);
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became Manager successfully", user, HttpStatus.OK.value()));
    }

    @PutMapping("/make-user/{id}")
    @Operation(summary = "Change User Role to User", description = "Change the role of a user to User")
    @ApiResponse(responseCode = "200", description = "User role changed to User successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeToUser(@PathVariable("id") Long id) {
        LOGGER.info("Received request to change user with ID {} to User", id);
        UserDto user = userService.changeRole(id, "USER");
        LOGGER.info("User with ID {} changed to User successfully", id);
        return ResponseEntity.ok(new SuccessResponse(user.getFirstName() + " became User successfully", user, HttpStatus.OK.value()));
    }

    @PutMapping("/subscribe-status/{id}")
    @Operation(summary = "Change User subscribe status to User", description = "Change the subscribe status of a user")
    @ApiResponse(responseCode = "200", description = "User status changed to User successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<SuccessResponse> changeStatus(@PathVariable("id") Long id) {
        LOGGER.info("Received request to change status to ID user with id {} ", id);
        User user = userService.changeSubscribeStatus(id);
        LOGGER.info("User with ID {} changed to status successfully", id);
        return ResponseEntity.ok(new SuccessResponse("change status success", user.getSubscribeStatus(), HttpStatus.OK.value()));
    }
}

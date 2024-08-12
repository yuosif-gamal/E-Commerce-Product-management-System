package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.UserDto;
import com.example.Ecommerce.entity.Role;
import com.example.Ecommerce.exception.DuplicateResourceException;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.UserMapper;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public void register(UserDto request) {
        LOGGER.info("Registering user with email: {}", request.getEmail());

        User existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser != null) {
            LOGGER.error("Email is connected to another account: {}", request.getEmail());
            throw new DuplicateResourceException("Email is connected to another account.");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserMapper.convertDtoToEntity(request);
        Role userRole = roleService.findRoleByName("USER");
        user.addRole(userRole);
        userRepo.save(user);

        LOGGER.info("User registered successfully: {}", user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        LOGGER.info("Retrieving all users");

        List<User> userList = userRepo.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = UserMapper.convertEntityToDto(user);
            userDtoList.add(userDto);
        }

        LOGGER.info("Total users retrieved: {}", userDtoList.size());
        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        LOGGER.info("Retrieving user by ID: {}", userId);

        User user = userRepo.getUserById(userId);
        if (user == null) {
            LOGGER.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        UserDto userDto = UserMapper.convertEntityToDto(user);
        LOGGER.info("User retrieved successfully: {}", userDto);
        return userDto;
    }

    @Override
    public UserDto deleteUserById(Long userId) {
        LOGGER.info("Deleting user by ID: {}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found with id: " + userId);
                });

        userRepo.deleteById(userId);
        UserDto userDto = UserMapper.convertEntityToDto(user);
        LOGGER.info("User deleted successfully: {}", userDto);
        return userDto;
    }

    @Override
    @Cacheable(value = "user")
    public User currentUser() {
        LOGGER.info("Retrieving current user");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            LOGGER.error("User not authenticated");
            throw new ResourceNotFoundException("User not authenticated");
        }

        String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            LOGGER.error("User not found with email: {}", email);
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        LOGGER.info("Current user retrieved: {}", user);
        return user;
    }

    @Override
    public UserDto changeRole(Long id, String myRole) {
        LOGGER.info("Changing role for user ID: {} to role: {}", id, myRole);

        User user = userRepo.findById(id).orElseThrow(() -> {
            LOGGER.error("No user found with ID: {}", id);
            return new ResourceNotFoundException("No user found with id: " + id);
        });

        Role role = roleService.findRoleByName(myRole);
        user.getRoles().clear();
        user.addRole(role);
        userRepo.save(user);

        UserDto userDto = UserMapper.convertEntityToDto(user);
        LOGGER.info("User role changed successfully: {}", userDto);
        return userDto;
    }
}

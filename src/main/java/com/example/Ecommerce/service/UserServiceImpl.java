package com.example.Ecommerce.service;

import com.example.Ecommerce.dto.UserDto;
import com.example.Ecommerce.entity.Role;
import com.example.Ecommerce.exception.DuplicateResourceException;
import com.example.Ecommerce.exception.ResourceNotFoundException;
import com.example.Ecommerce.mapper.UserMapper;
import com.example.Ecommerce.entity.User;
import com.example.Ecommerce.repository.UserRepo;
import com.example.Ecommerce.util.NotificationClient;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.Ecommerce.entity.UserSubscribeStatus.SUBSCRIBED;
import static com.example.Ecommerce.entity.UserSubscribeStatus.UNSUBSCRIBED;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final NotificationClient notificationClient;

    @Override
    public void register(UserDto request) {

        User existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new DuplicateResourceException("Email is connected to another account.");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = UserMapper.convertDtoToEntity(request);
        Role userRole = roleService.findRoleByName("USER");
        user.addRole(userRole);
        user.setSubscribeStatus(SUBSCRIBED);
        userRepo.save(user);
        notificationClient.registrationCompleteEmail(request.getEmail(), request.getFirstName(), user.getId());
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> userList = userRepo.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = UserMapper.convertEntityToDto(user);
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = isExist(userId);
        return UserMapper.convertEntityToDto(user);
    }

    @Override
    public UserDto deleteUserById(Long userId) {

        User user = isExist(userId);

        userRepo.deleteById(userId);
        return UserMapper.convertEntityToDto(user);
    }

    @Override
    @Cacheable(value = "user")
    public User currentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }

        String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        return user;
    }

    @Override
    public UserDto changeRole(Long id, String myRole) {

        User user = isExist(id);

        Role role = roleService.findRoleByName(myRole);
        user.getRoles().clear();
        user.addRole(role);
        userRepo.save(user);

        return UserMapper.convertEntityToDto(user);
    }

    @Override
    public User changeSubscribeStatus(Long id) {
        User user = isExist(id);

        if (user.getSubscribeStatus() == SUBSCRIBED) {
            user.setSubscribeStatus(UNSUBSCRIBED);
        } else {
            user.setSubscribeStatus(SUBSCRIBED);
        }
        userRepo.save(user);
        return user;
    }

    private User isExist(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + id));
    }
}

package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.model.RegistrationRequest;
import com.example.producttestapi.entities.Role;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final RoleService roleService;

    public UserServiceImpl(UserRepo userRepo, RoleService roleService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
    }

    @Override
    public void createUser(RegistrationRequest request) {
        User user = new User(request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail());
        Role userRole = roleService.findRoleByName("USER");
        user.addRole(userRole);
        userRepo.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean UserExistsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}

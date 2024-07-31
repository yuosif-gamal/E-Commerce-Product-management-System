package com.example.producttestapi.service;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CartRepo;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    @Autowired
    public CartServiceImpl(CartRepo cartRepo, UserRepo userRepoRepo){
        this.cartRepo =cartRepo;
        this.userRepo = userRepoRepo;
    }
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
        User user = userRepo.findByEmail(email);
        return user;
    }

    @Override
    @Cacheable(value = "cart")
    public Cart getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found for user with name: " + user.getFirstName());
        }
        return cart;
    }
}


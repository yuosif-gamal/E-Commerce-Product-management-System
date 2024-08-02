package com.example.producttestapi.service;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CartRepo;
import com.example.producttestapi.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    @Autowired
    public CartServiceImpl(CartRepo cartRepo, UserRepo userRepoRepo, UserService userRepService){
        this.cartRepo = cartRepo;
        this.userRepo = userRepoRepo;
        this.userService = userRepService;
    }

    @Override
    @Cacheable(value = "cart")
    public Cart getCart() {
        User user = userService.currentUser();
        Cart cart = cartRepo.findByUser(user);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found for user with name: " + user.getFirstName());
        }
        return cart;
    }
}

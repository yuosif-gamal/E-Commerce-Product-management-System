package com.example.producttestapi.repository;

import com.example.producttestapi.entity.Cart;
import com.example.producttestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}

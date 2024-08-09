package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Cart;
import com.example.Ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}

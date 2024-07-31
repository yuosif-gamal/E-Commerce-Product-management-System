package com.example.producttestapi.repos;

import com.example.producttestapi.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem , Integer> {
}

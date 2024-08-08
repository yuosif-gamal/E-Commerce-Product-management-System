package com.example.producttestapi.repository;

import com.example.producttestapi.entity.CartItem;
import com.example.producttestapi.entity.Product;
import com.example.producttestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem , Long> {

    CartItem findByProductAndCart_User(Product product, User user);
}

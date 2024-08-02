package com.example.producttestapi.repos;

import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem , Integer> {

    CartItem findByProductAndCart_User(Product product, User user);
}

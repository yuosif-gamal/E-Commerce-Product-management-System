package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.CartItem;
import com.example.Ecommerce.entity.Product;
import com.example.Ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem , Long> {

    CartItem findByProductAndCart_User(Product product, User user);
}

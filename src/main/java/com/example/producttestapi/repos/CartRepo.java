package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    Cart findByUser(User user);

    void deleteById(int id);
}

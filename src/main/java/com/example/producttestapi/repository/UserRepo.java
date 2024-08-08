package com.example.producttestapi.repository;

import com.example.producttestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String  email);
    User getUserById(int id);
    User findAllByEmail(String email);
    void deleteById(int id);
}

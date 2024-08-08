package com.example.producttestapi.repository;

import com.example.producttestapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String  email);
    User getUserById(Long id);
    User findAllByEmail(String email);
    void deleteById(Long id);
}

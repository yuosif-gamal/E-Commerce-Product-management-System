package com.example.producttestapi.repos;

import com.example.producttestapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String  email);
    User getUserById(Long id);
    User findAllByEmail(String email);
    void deleteById(Long id);
}

package com.example.producttestapi.repos;

import com.example.producttestapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String  email);
    User getUserById(int id);
    User findAllByEmail(String email);
    void deleteById(int id);
}

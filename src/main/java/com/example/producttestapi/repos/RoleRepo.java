package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    public Role findByName(String name);

    Role deleteByName(String name);
}

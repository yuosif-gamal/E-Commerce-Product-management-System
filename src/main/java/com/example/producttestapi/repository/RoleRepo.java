package com.example.producttestapi.repository;

import com.example.producttestapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    public Role findByName(String name);

    Role deleteByName(String name);
}

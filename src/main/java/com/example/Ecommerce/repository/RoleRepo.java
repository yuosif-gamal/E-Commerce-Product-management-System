package com.example.Ecommerce.repository;

import com.example.Ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    public Role findByName(String name);

    Role deleteByName(String name);
}

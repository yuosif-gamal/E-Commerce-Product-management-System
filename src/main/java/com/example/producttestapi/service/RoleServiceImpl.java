package com.example.producttestapi.service;

import com.example.producttestapi.entities.Role;
import com.example.producttestapi.repos.RoleRepo;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name);
    }
}

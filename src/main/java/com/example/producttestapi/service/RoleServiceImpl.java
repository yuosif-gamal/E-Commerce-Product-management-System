package com.example.producttestapi.service;

import com.example.producttestapi.entity.Role;
import com.example.producttestapi.repository.RoleRepo;
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

    @Override
    public Role deleteRoleByName(String name) {
        return roleRepo.deleteByName(name);
    }
}

package com.example.producttestapi.service;

import com.example.producttestapi.entity.Role;
import com.example.producttestapi.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class RoleServiceImpl implements RoleService{
    private final RoleRepo roleRepo;



    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name);
    }

    @Override
    public Role deleteRoleByName(String name) {
        return roleRepo.deleteByName(name);
    }
}

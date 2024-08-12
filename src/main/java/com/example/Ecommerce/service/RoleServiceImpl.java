package com.example.Ecommerce.service;

import com.example.Ecommerce.entity.Role;
import com.example.Ecommerce.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepo roleRepo;

    @Override
    public Role findRoleByName(String name) {
        LOGGER.info("Searching for role by name: {}", name);

        Role role = roleRepo.findByName(name);
        if (role == null) {
            LOGGER.error("Role not found with name: {}", name);
        } else {
            LOGGER.info("Role found: {}", role);
        }

        return role;
    }

    @Override
    public Role deleteRoleByName(String name) {
        LOGGER.info("Deleting role by name: {}", name);

        Role role = roleRepo.findByName(name);
        if (role == null) {
            LOGGER.error("Role not found with name: {}", name);
            return null;
        }

        roleRepo.deleteByName(name);
        LOGGER.info("Role deleted successfully: {}", role);
        return role;
    }
}

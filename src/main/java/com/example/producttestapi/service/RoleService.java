package com.example.producttestapi.service;

import com.example.producttestapi.entities.Role;

public interface RoleService {
    Role findRoleByName(String name);

    Role deleteRoleByName(String name);
}

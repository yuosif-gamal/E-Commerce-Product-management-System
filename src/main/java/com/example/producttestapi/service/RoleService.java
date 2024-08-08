package com.example.producttestapi.service;

import com.example.producttestapi.entity.Role;

public interface RoleService {
    Role findRoleByName(String name);

    Role deleteRoleByName(String name);
}

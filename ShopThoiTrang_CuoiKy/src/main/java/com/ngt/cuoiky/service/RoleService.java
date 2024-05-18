package com.ngt.cuoiky.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Role;
import com.ngt.cuoiky.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRoleByID(int id) {
        Role role = roleRepository.getRoleByID(id);
        return role;
    }
}

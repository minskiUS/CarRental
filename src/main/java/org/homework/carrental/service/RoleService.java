package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Role;
import org.homework.carrental.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public Role getUserRole() {
        return getRole("USER");
    }

    public Role getRoleByName(String role) {
        return getRole(role);
    }

    private Role getRole(String role) {
        return roleRepository.findAll().stream()
                .filter(r -> role.equals(r.getName()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Role not found"));
    }
}

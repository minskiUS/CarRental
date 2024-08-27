package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Role;
import org.homework.carrental.model.User;
import org.homework.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private RoleService roleService;

    public void promote(UUID uid, String role) {
        User user = getUser(uid);
        Set<Role> roles = user.getRoles();
        Role r = roleService.getRoleByName(role);
        roles.add(r);

        userRepository.save(user);
    }

    public void demote(UUID uid, String role) {
        User user = getUser(uid);
        Set<Role> roles = user.getRoles();
        Role r = roleService.getRoleByName(role);
        if (roles.contains(r)) {
            roles.remove(r);
            userRepository.save(user);
        }
    }

    public void lock(UUID uid) {
        User user = getUser(uid);
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void unlock(UUID uid) {
        User user = getUser(uid);
        user.setEnabled(true);
        userRepository.save(user);
    }

    private User getUser(UUID uid) {
        Optional<User> byId = userRepository.findById(uid);
        if (byId.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        return byId.get();
    }
}

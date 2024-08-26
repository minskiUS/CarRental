package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.dto.LoginUserDto;
import org.homework.carrental.dto.RegisterUserDto;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Role;
import org.homework.carrental.model.User;
import org.homework.carrental.repository.RoleRepository;
import org.homework.carrental.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setEmail(input.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setAge(input.getAge());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setId(UUID.randomUUID());

        Role roleUser = roleRepository.findAll().stream()
                .filter(role -> "USER".equals(role.getName()))
                .findFirst().orElseThrow(() -> new NotFoundException("Role not found"));
        user.setRoles(Set.of(roleUser));


        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

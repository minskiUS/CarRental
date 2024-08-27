package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.dto.LoginUserDto;
import org.homework.carrental.dto.RegisterUserDto;
import org.homework.carrental.dto.UserDto;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.mapper.UserMapper;
import org.homework.carrental.model.Role;
import org.homework.carrental.model.User;
import org.homework.carrental.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @Transactional
    public UserDto signup(RegisterUserDto input) {
        Role userRole = roleService.getUserRole();
        User user = User.builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .age(input.getAge())
                .id(UUID.randomUUID())
                .roles(Set.of(userRole))
                .enabled(true)
                .build();
        User save = userRepository.save(user);
        return userMapper.toUserDto(save);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}

package org.homework.carrental.service;

import org.homework.carrental.dto.LoginUserDto;
import org.homework.carrental.dto.RegisterUserDto;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.mapper.UserMapper;
import org.homework.carrental.model.Role;
import org.homework.carrental.model.User;
import org.homework.carrental.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private final String email = "email@email.com";
    private final String password = "password";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthenticationService testee;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void signUp_shouldThrowException_WhenRoleNotFound() {
        when(roleService.getUserRole()).thenThrow(new NotFoundException("Role not found"));
        RegisterUserDto registerUserDto = RegisterUserDto.builder().build();

        assertThrows(NotFoundException.class, () -> testee.signup(registerUserDto));
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void signUp_shouldSaveUser_WhenCalled() {
        when(roleService.getUserRole()).thenReturn(new Role(1L, "USER"));
        when(passwordEncoder.encode(password)).thenReturn(password);
        RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .email(email)
                .password(password)
                .build();

        testee.signup(registerUserDto);

        verify(passwordEncoder).encode(password);
        verify(roleService).getUserRole();
        verify(userRepository).save(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email(email)
                .build();
        assertThrows(NotFoundException.class, () -> testee.authenticate(loginUserDto));
    }

    @Test
    void authenticate_ShouldReturnToken_WhenUserFind() {
        User expectedUser = User.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email(email)
                .build();

        User actual = testee.authenticate(loginUserDto);
        assertEquals(expectedUser, actual);
    }
}
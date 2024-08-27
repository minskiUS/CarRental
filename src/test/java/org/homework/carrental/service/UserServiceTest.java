package org.homework.carrental.service;

import org.homework.carrental.exception.NotFoundException;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UUID userId = UUID.randomUUID();
    private Role role = new Role(1L, "USER");

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService testee;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void test_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () ->testee.lock(userId));
    }

    @Test
    void lock_ShouldLockUser() {
        User user = new User();
        user.setEnabled(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        testee.lock(userId);

        verify(userRepository).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertFalse(value.isEnabled());
    }

    @Test
    void unlock_ShouldLockUser() {
        User user = new User();
        user.setEnabled(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        testee.unlock(userId);

        verify(userRepository).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertTrue(value.isEnabled());
    }

    @Test
    void demote_ShouldRemovePermission() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleService.getRoleByName("USER")).thenReturn(role);

        testee.demote(userId, "USER");

        verify(userRepository).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertEquals(0, value.getRoles().size());
    }

    @Test
    void promote_ShouldAddPermission() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleService.getRoleByName("USER")).thenReturn(role);

        testee.promote(userId, "USER");

        verify(userRepository).save(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        assertEquals(1, value.getRoles().size());
    }
}
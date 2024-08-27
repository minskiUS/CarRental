package org.homework.carrental.service;

import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Role;
import org.homework.carrental.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private Role role = new Role(1L, "USER");

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService testee;

    @BeforeEach
    void setUp() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
    }

    @Test
    void getRoleByName_ShouldThrowException_WhenRoleNotFound() {
        assertThrows(NotFoundException.class, () -> testee.getRoleByName("NOT_USER"));
    }

    @Test
    void getRoleByName_ShouldReturnRole_WhenFound() {
        Role user = testee.getRoleByName("USER");
        assertEquals(1L, user.getId());
    }

    @Test
    void getUserRole_ShouldReturnRole_WhenFound() {
        Role user = testee.getUserRole();
        assertEquals(1L, user.getId());
    }
}
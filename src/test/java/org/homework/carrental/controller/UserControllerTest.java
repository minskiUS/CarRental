package org.homework.carrental.controller;

import org.homework.carrental.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void promote_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "ROLE_USER";

        mockMvc.perform(post("/api/v1/admin/promote/{uid}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).promote(any(UUID.class), any(String.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void demote_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "ROLE_USER";

        mockMvc.perform(post("/api/v1/admin/demote/{uid}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).demote(any(UUID.class), any(String.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void lock_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/admin/lock/{uid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).lock(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void unlock_shouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/admin/unlock/{uid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).unlock(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void adminEndpoints_shouldReturnForbiddenForNonAdminUser() throws Exception {
        UUID userId = UUID.randomUUID();
        String role = "ROLE_USER";

        mockMvc.perform(post("/api/v1/admin/promote/{uid}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/admin/demote/{uid}/{role}", userId, role)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/admin/lock/{uid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/admin/unlock/{uid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

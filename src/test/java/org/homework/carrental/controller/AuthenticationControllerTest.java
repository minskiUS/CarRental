package org.homework.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.homework.carrental.dto.LoginUserDto;
import org.homework.carrental.dto.RegisterUserDto;
import org.homework.carrental.dto.UserDto;
import org.homework.carrental.model.User;
import org.homework.carrental.service.AuthenticationService;
import org.homework.carrental.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Test
    void authenticate_ShouldGiveToken_WhenFound() throws Exception {
        LoginUserDto loginUserDto = new LoginUserDto("username", "password");
        User authenticatedUser = User.builder()
                .email("username")
                .password("password")
                .build();

        when(authenticationService.authenticate(loginUserDto)).thenReturn(authenticatedUser);

        when(jwtService.generateToken(authenticatedUser)).thenReturn("mocked-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        String requestBody = new ObjectMapper().writeValueAsString(loginUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));

        verify(authenticationService).authenticate(loginUserDto);
        verify(jwtService).generateToken(authenticatedUser);
    }

    @Test
    void shouldRegisterUserAndReturnUserDto() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto(
                "test@example.com",
                "Password123!",
                "John",
                "Doe",
                (short) 30
        );

        UserDto userDto = new UserDto(
                "test@example.com",
                "John",
                "Doe",
                (short) 30
        );

        when(authenticationService.signup(registerUserDto))
                .thenReturn(userDto);

        String requestBody = new ObjectMapper().writeValueAsString(registerUserDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));

        verify(authenticationService).signup(registerUserDto);
    }

}
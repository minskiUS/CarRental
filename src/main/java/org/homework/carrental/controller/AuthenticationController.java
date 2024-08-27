package org.homework.carrental.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.homework.carrental.dto.LoginResponse;
import org.homework.carrental.dto.LoginUserDto;
import org.homework.carrental.dto.RegisterUserDto;
import org.homework.carrental.dto.UserDto;
import org.homework.carrental.model.User;
import org.homework.carrental.service.AuthenticationService;
import org.homework.carrental.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto userDto = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
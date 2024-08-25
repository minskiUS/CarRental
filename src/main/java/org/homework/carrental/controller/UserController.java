package org.homework.carrental.controller;

import org.homework.carrental.model.User;
import org.homework.carrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ff")
    public ResponseEntity<List<User>> dff() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


}

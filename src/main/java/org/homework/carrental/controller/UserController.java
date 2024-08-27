package org.homework.carrental.controller;

import lombok.AllArgsConstructor;
import org.homework.carrental.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class UserController {

    private UserService userService;

    @PostMapping("/promote/{uid}/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> promote(@PathVariable UUID uid, @PathVariable String role) {
        userService.promote(uid, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/demote/{uid}/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> demote(@PathVariable UUID uid, @PathVariable String role) {
        userService.demote(uid, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lock/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> lock(@PathVariable UUID uid) {
        userService.lock(uid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlock/{uid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unlock(@PathVariable UUID uid) {
        userService.unlock(uid);
        return ResponseEntity.ok().build();
    }


}

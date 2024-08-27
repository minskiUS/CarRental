package org.homework.carrental.controller;

import lombok.AllArgsConstructor;
import org.homework.carrental.dto.BookingRequestDto;
import org.homework.carrental.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {

    private BookingService bookingService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelReservation(id, userDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> make(@RequestBody BookingRequestDto booking, @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.makeReservation(booking, userDetails);
        return ResponseEntity.ok().build();
    }
}

package org.homework.carrental.controller;

import lombok.AllArgsConstructor;
import org.homework.carrental.model.Car;
import org.homework.carrental.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car")
public class CarManagementController {

    private CarService carService;

    @GetMapping
    public ResponseEntity<?> findById(@RequestParam(value = "id", required = false) UUID id) {
        if (id == null) {
            return ResponseEntity.ok(carService.findAll());
        }
        return ResponseEntity.ok(carService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car car) {
        return ResponseEntity.ok(carService.save(car));
    }

    @PostMapping("/availability/{id}/{isAvailable}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> availability(@PathVariable UUID id, @PathVariable boolean isAvailable) {
        carService.availability(id, isAvailable);
        return ResponseEntity.noContent().build();
    }
}

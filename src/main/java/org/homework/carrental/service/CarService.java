package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Car;
import org.homework.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class CarService {

    private CarRepository carRepository;

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car findById(UUID id) {
        return carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found"));
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public void availability(UUID id, boolean isAvailable) {
        Car car = carRepository.findById(id).orElseThrow(() -> new NotFoundException("Car not found"));
        car.setAvailable(isAvailable);
        carRepository.save(car);
    }
}

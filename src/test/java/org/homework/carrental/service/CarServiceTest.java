package org.homework.carrental.service;

import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Car;
import org.homework.carrental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void findAll_ShouldReturnCars() {
        Car car1 = new Car(UUID.randomUUID(), "Model S", "Tesla", "2023", 100, true);
        Car car2 = new Car(UUID.randomUUID(), "Model 3", "Tesla", "2022", 50, true);
        List<Car> carList = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(carList);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        verify(carRepository).findAll();
    }

    @Test
    void findById__ShouldReturnCar_WhenExists() {
        UUID carId = UUID.randomUUID();
        Car car = new Car(carId, "Model S", "Tesla", "2023", 100, true);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car result = carService.findById(carId);

        assertNotNull(result);
        assertEquals("Model S", result.getModel());
        verify(carRepository).findById(carId);
    }

    @Test
    void testFindById_whenCarDoesNotExist() {
        UUID carId = UUID.randomUUID();

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> carService.findById(carId));

        assertEquals("Car not found", exception.getMessage());
        verify(carRepository).findById(carId);
    }

    @Test
    void save_ShouldSave_WhenCalled() {
        Car car = new Car(UUID.randomUUID(), "Model S", "Tesla", "2023", 100, true);

        when(carRepository.save(car)).thenReturn(car);

        Car result = carService.save(car);

        assertNotNull(result);
        assertEquals("Model S", result.getModel());
        verify(carRepository).save(car);
    }

    @Test
    void availability_ShouldChangeAvailability_WhenCalled() {
        UUID carId = UUID.randomUUID();
        Car car = new Car(carId, "Model S", "Tesla", "2023", 100, false);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);

        carService.availability(carId, true);

        assertTrue(car.isAvailable());
        verify(carRepository).findById(carId);
        verify(carRepository).save(car);
    }

    @Test
    void availability_ShouldThrowException_WhenCarNotFound() {
        UUID carId = UUID.randomUUID();

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> carService.availability(carId, true));

        assertEquals("Car not found", exception.getMessage());
        verify(carRepository).findById(carId);
        verifyNoMoreInteractions(carRepository);
    }
}

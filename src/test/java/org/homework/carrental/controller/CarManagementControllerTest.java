package org.homework.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.homework.carrental.model.Car;
import org.homework.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CarManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @InjectMocks
    private CarManagementController carManagementController;

    @BeforeEach
    void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Test
    void findById_withNoId_shouldReturnAllCars() throws Exception {
        mockMvc.perform(get("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService).findAll();
    }

    @Test
    void findById_withId_shouldReturnCar() throws Exception {
        UUID carId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/car")
                        .param("id", carId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService).findById(carId);
    }

    @Test
    void save_shouldSaveCarAndReturnIt() throws Exception {
        Car car = new Car();
        car.setId(UUID.randomUUID());
        car.setModel("Model S");
        car.setMake("Tesla");
        car.setYear("2023");
        car.setMileage(100);
        car.setAvailable(true);

        when(carService.save(car)).thenReturn(car);

        String carJson = new ObjectMapper().writeValueAsString(car);

        mockMvc.perform(post("/api/v1/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Model S"))
                .andExpect(jsonPath("$.make").value("Tesla"))
                .andExpect(jsonPath("$.year").value("2023"));

        verify(carService).save(car);
    }

    @Test
    void availability_shouldUpdateCarAvailability() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        UUID carId = UUID.randomUUID();
        boolean isAvailable = true;

        mockMvc.perform(post("/api/v1/car/availability/{id}/{isAvailable}", carId, isAvailable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(carService).availability(carId, isAvailable);
    }

    @Test
    void availability_shouldReturnForbiddenForNonAdmin() throws Exception {
        UUID carId = UUID.randomUUID();
        boolean isAvailable = true;

        mockMvc.perform(post("/api/v1/car/availability/{id}/{isAvailable}", carId, isAvailable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}


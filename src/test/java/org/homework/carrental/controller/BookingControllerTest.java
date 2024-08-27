package org.homework.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.homework.carrental.dto.BookingRequestDto;
import org.homework.carrental.service.BookingService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserDetails userDetails;

    @InjectMocks
    private BookingController bookingController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void cancel_shouldReturnOk() throws Exception {
        UUID bookingId = UUID.randomUUID();

        doNothing().when(bookingService).cancelReservation(any(UUID.class), any(UserDetails.class));

        mockMvc.perform(delete("/api/v1/booking/{id}", bookingId)
                        .principal(() -> "user@example.com"))
                .andExpect(status().isOk());

        verify(bookingService).cancelReservation(bookingId, null);
    }

    @Test
    void make_shouldReturnOk() throws Exception {
        BookingRequestDto booking = new BookingRequestDto(UUID.randomUUID(), LocalDate.now(), LocalDate.now());

        doNothing().when(bookingService).makeReservation(any(BookingRequestDto.class), any(UserDetails.class));

        String bookingJson = objectMapper.writeValueAsString(booking);

        mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
                        .principal(() -> "user@example.com"))  // Simulate the principal
                .andExpect(status().isOk());

        verify(bookingService).makeReservation(booking, null);
    }
}

package org.homework.carrental.service;

import lombok.AllArgsConstructor;
import org.homework.carrental.dto.BookingRequestDto;
import org.homework.carrental.exception.BadRequestException;
import org.homework.carrental.exception.NotFoundException;
import org.homework.carrental.model.Booking;
import org.homework.carrental.model.Car;
import org.homework.carrental.model.User;
import org.homework.carrental.repository.BookingRepository;
import org.homework.carrental.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private CarService carService;

    public void cancelReservation(UUID id, UserDetails userDetails) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Booking not found"));
        if (canPerformAction(userDetails, booking)) {
            bookingRepository.delete(booking);
            return;
        }
        throw new BadRequestException("You can delete only your booking");
    }

    public void makeReservation(BookingRequestDto booking, UserDetails userDetails) {
        LocalDate startDate = booking.getStartDate();
        LocalDate endDate = booking.getEndDate();
        if (startDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Start date cannot be before end date");
        }
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("End date cannot be before start date");
        }
        UUID carId = booking.getCarId();
        List<Booking> allByCarId = bookingRepository.findBookingsByCarIdAndDateRange(carId, startDate, endDate);
        if (!allByCarId.isEmpty()) {
            throw new BadRequestException("This booking already exists");
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("user not found"));
        Car car = carService.findById(booking.getCarId());
        Booking bookingToSave = Booking.builder()
                .startDate(startDate)
                .endDate(endDate)
                .price(calculatePrice(startDate, endDate))
                .user(user)
                .car(car)
                .build();
        bookingRepository.save(bookingToSave);
    }

    public BigDecimal calculatePrice(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        long hours = days * 24;
        BigDecimal hourlyRate = new BigDecimal("10000");

        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }

    private boolean canPerformAction(UserDetails userDetails, Booking booking) {
        return userDetails.getUsername().equals(booking.getUser().getUsername()) ||
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}

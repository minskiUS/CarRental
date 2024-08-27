package org.homework.carrental.repository;

import org.homework.carrental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId AND b.startDate >= :startDate AND b.endDate <= :endDate")
    List<Booking> findBookingsByCarIdAndDateRange(
            @Param("carId") UUID carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

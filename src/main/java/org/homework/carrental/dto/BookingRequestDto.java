package org.homework.carrental.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BookingRequestDto {

    private UUID carId;
    private LocalDate startDate;
    private LocalDate endDate;
}

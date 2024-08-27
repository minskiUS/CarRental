package org.homework.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car", schema = "carrent")
public class Car {

    @Id
    private UUID id;
    private String model;
    private String make;
    private String year;
    private int mileage;
    private boolean isAvailable;
}

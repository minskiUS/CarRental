package org.homework.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "user", schema = "carrent")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private short age;
}

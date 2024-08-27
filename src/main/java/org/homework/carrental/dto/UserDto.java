package org.homework.carrental.dto;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String firstName;
    private String lastName;
    private short age;
}

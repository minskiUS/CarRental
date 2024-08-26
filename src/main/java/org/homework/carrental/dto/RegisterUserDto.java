package org.homework.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RegisterUserDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private short age;

}

package org.homework.carrental.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RegisterUserDto {

    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Min(value = 21, message = "Valid age is between 21 and 65")
    @Max(value = 65, message = "Valid age is between 21 and 65")
    private short age;

}

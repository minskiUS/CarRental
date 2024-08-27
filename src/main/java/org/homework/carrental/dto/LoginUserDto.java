package org.homework.carrental.dto;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class LoginUserDto {

    private String email;
    private String password;
}

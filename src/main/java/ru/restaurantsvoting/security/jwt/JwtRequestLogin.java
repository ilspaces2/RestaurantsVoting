package ru.restaurantsvoting.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JwtRequestLogin {

    private final String email;

    private final String password;
}

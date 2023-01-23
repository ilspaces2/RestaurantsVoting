package ru.restaurantsvoting.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class JwtRequest {

    private final String email;

    private final String password;
}

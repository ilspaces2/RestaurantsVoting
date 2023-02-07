package ru.restaurantsvoting.security.jwt;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class JwtResponse {

    private final String type = "Bearer";

    private String accessToken;
}

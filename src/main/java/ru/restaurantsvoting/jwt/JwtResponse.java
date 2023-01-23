package ru.restaurantsvoting.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtResponse {

    private final String type;

    private final String accessToken;

    private final String refreshToken;
}

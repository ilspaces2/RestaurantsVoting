package ru.restaurantsvoting.security.jwt;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class JwtResponse {

    private final String type = "Bearer ";

    private String accessToken;
}

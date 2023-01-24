package ru.restaurantsvoting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.jwt.JwtRequestLogin;
import ru.restaurantsvoting.jwt.JwtRequest;
import ru.restaurantsvoting.jwt.JwtResponse;
import ru.restaurantsvoting.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody JwtRequestLogin authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse getAccessToken(@RequestBody JwtRequest refreshToken) {
        return authService.getNewAccessToken(refreshToken.getToken());
    }

    @PostMapping("refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse refreshToken(@RequestBody JwtRequest refreshToken) {
        return authService.refreshToken(refreshToken.getToken());
    }
}

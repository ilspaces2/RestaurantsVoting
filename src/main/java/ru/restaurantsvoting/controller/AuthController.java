package ru.restaurantsvoting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.restaurantsvoting.security.jwt.JwtRequestLogin;
import ru.restaurantsvoting.security.jwt.JwtResponse;
import ru.restaurantsvoting.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody JwtRequestLogin authRequest) {
        return authService.login(authRequest);
    }
}

package ru.restaurantsvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.restaurantsvoting.jwt.JwtProvider;
import ru.restaurantsvoting.jwt.JwtRequestLogin;
import ru.restaurantsvoting.jwt.JwtResponse;
import ru.restaurantsvoting.security.AuthUser;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public JwtResponse login(JwtRequestLogin authRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        String accessToken = jwtProvider.generateAccessToken((AuthUser) auth.getPrincipal());
        return new JwtResponse(accessToken);
    }
}

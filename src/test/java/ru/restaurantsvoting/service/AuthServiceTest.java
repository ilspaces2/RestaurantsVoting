package ru.restaurantsvoting.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import ru.restaurantsvoting.security.jwt.JwtProvider;
import ru.restaurantsvoting.security.jwt.JwtRequestLogin;
import ru.restaurantsvoting.security.jwt.JwtResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    /**
     * Проверка на совпадение токенов проходит.
     * Как тестировать что юзер не прошел проверку authenticate пока не понятно...
     */
    @Test
    void login() {
        JwtResponse token = new JwtResponse("token");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtProvider.generateAccessToken(any())).thenReturn(token.getAccessToken());
        JwtResponse actualResult = authService.login(new JwtRequestLogin("user", "password"));
        Assertions.assertThat(actualResult).isEqualTo(token);
    }
}
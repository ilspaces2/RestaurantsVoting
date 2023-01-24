package ru.restaurantsvoting.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.restaurantsvoting.jwt.JwtProvider;
import ru.restaurantsvoting.jwt.JwtRequest;
import ru.restaurantsvoting.jwt.JwtResponse;
import ru.restaurantsvoting.model.Token;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.TokenRepository;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;

    public JwtResponse login(JwtRequest authRequest) {
        User user = getUserByEmail(authRequest.getEmail());
        if (!user.getPassword().equals(passwordEncoder.encode(authRequest.getPassword()))) {
            throw new NotFoundException("Неправильный пароль");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        tokenRepository.save(new Token(user.getEmail(), refreshToken));
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getNewAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String savedRefreshToken = tokenRepository.findByEmail(email).orElseThrow().getRefreshToken();
            if (savedRefreshToken.equals(refreshToken)) {
                User user = getUserByEmail(email);
                String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            String savedRefreshToken = tokenRepository.findByEmail(email).orElseThrow().getRefreshToken();
            if (savedRefreshToken.equals(refreshToken)) {
                User user = getUserByEmail(email);
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                tokenRepository.save(new Token(user.getEmail(), refreshToken));
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new NoSuchElementException("Невалидный JWT токен");
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    /*
        public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        }
     */
}

package ru.restaurantsvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.restaurantsvoting.jwt.JwtProvider;
import ru.restaurantsvoting.jwt.JwtRequestLogin;
import ru.restaurantsvoting.jwt.JwtResponse;
import ru.restaurantsvoting.model.Token;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.TokenRepository;
import ru.restaurantsvoting.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public JwtResponse login(JwtRequestLogin authRequest) {
        String email = authRequest.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, authRequest.getPassword()));
        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);
        tokenRepository.save(new Token(email, refreshToken));
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getNewAccessToken(String refreshToken) {
        return generateTokens(refreshToken, false);
    }

    public JwtResponse refreshToken(String refreshToken) {
        return generateTokens(refreshToken, true);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private JwtResponse generateTokens(String token, boolean refresh) {
        if (!jwtProvider.validateRefreshToken(token)) {
            throw new NoSuchElementException("Невалидный JWT токен");
        }
        String email = jwtProvider.getRefreshClaims(token).getSubject();
        String savedRefreshToken = tokenRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Token not found"))
                .getRefreshToken();
        if (savedRefreshToken.equals(token)) {
//            User user = getUserByEmail(email);
            String accessToken = jwtProvider.generateAccessToken(email);
            if (refresh) {
                String refreshToken = jwtProvider.generateAccessToken(email);
                tokenRepository.save(new Token(email, refreshToken));
                return new JwtResponse(accessToken, refreshToken);
            }
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }
}

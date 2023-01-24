package ru.restaurantsvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;

    public JwtResponse login(JwtRequestLogin authRequest) {
        User user = getUserByEmail(authRequest.getEmail());
//        if (!user.getPassword().equals(passwordEncoder.encode(authRequest.getPassword()))) {
//            throw new NotFoundException("Неправильный пароль");
//        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        tokenRepository.save(new Token(user.getEmail(), refreshToken));
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
            User user = getUserByEmail(email);
            String accessToken = jwtProvider.generateAccessToken(user);
            if (refresh) {
                String refreshToken = jwtProvider.generateAccessToken(user);
                tokenRepository.save(new Token(user.getEmail(), refreshToken));
                return new JwtResponse(accessToken, refreshToken);
            }
            return new JwtResponse(accessToken, null);
        }
        return new JwtResponse(null, null);
    }
}

package ru.restaurantsvoting.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.restaurantsvoting.model.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    private final SecretKey jwtRefreshSecret;

    @Value("${jwt.access.expiration.timeOfMinutes}")
    private int accessExpirationTime;

    @Value("${jwt.refresh.expiration.timeOfMinutes}")
    private int refreshExpirationTime;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtAccessSecret,
                       @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(generateExpirationTime(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRoles())
                .claim("name", user.getName())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(generateExpirationTime(refreshExpirationTime))
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException e) {
            log.error("Error validate token: {}", e.getMessage());
        }
        return false;
    }

    private Date generateExpirationTime(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        Instant expirationInstant = now.plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(expirationInstant);
    }
}

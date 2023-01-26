package ru.restaurantsvoting.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.restaurantsvoting.security.AuthUser;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@Setter
public class JwtProvider {

    private SecretKey jwtAccessSecret;

    private int accessExpirationTime;

    public JwtProvider(@Value("${access}") String jwtAccessSecret,
                       @Value("${accessTimeOfSeconds}") int accessExpirationTime) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.accessExpirationTime = accessExpirationTime;
    }

    public String generateAccessToken(AuthUser authUser) {
        return Jwts.builder()
                .setSubject(authUser.getUsername())
                .setExpiration(generateExpirationTime(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .claim("roles", authUser.getAuthorities())
                .claim("id", authUser.getId())
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException e) {
            log.error("Error validate token: {}", e.getMessage());
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtAccessSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateExpirationTime(long seconds) {
        LocalDateTime now = LocalDateTime.now();
        Instant expirationInstant = now.plusSeconds(seconds).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(expirationInstant);
    }
}

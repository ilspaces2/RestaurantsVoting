package ru.restaurantsvoting.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private SecretKey jwtAccessSecret;

    private SecretKey jwtRefreshSecret;

    private int accessExpirationTime;

    private int refreshExpirationTime;

    private final UserDetailsService userDetailsService;

    public JwtProvider(@Value("${access}") String jwtAccessSecret,
                       @Value("${refresh}") String jwtRefreshSecret,
                       @Value("${accessTimeOfMinutes}") int accessExpirationTime,
                       @Value("${refreshTimeOfMinutes}") int refreshExpirationTime, UserDetailsService userDetailsService) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(generateExpirationTime(accessExpirationTime))
                .signWith(jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
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

    public Authentication getAuthentication(String token) {
        String login = getAccessClaims(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
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

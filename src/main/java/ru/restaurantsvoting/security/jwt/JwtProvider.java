package ru.restaurantsvoting.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.security.AuthUser;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Класс выполняющий основные манипуляции с токеном
 */
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

    public boolean validateAccessToken(String token) throws JwtException {
//        try {
        Jwts.parserBuilder()
                .setSigningKey(jwtAccessSecret)
                .build()
                .parseClaimsJws(token);
        return true;
//        } catch (JwtException e) {
//            log.error("Error validate token: {}", e.getMessage());
//        }
//        return false;
    }

    public Claims getAccessClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtAccessSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Id распарсил что бы было легче юзать UserService:) (лень)
     */
    public Authentication getAuthUserFromToken(String token) {
        Claims claims = getAccessClaims(token);
        String login = claims.getSubject();
        List<String> result = claims.get("roles", List.class);
        Set<Role> roles = result.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        int id = claims.get("id", Integer.class);
        User user = new User(login, "", false, roles);
        user.setId(id);
        return new UsernamePasswordAuthenticationToken(new AuthUser(user), "", roles);
    }

    private Date generateExpirationTime(long seconds) {
        LocalDateTime now = LocalDateTime.now();
        Instant expirationInstant = now.plusSeconds(seconds).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(expirationInstant);
    }
}

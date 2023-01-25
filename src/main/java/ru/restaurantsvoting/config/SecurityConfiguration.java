package ru.restaurantsvoting.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.restaurantsvoting.jwt.JwtFilter;
import ru.restaurantsvoting.model.Role;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
//https://stackoverflow.com/questions/72493425/548473
public class SecurityConfiguration {

    private static final String ADMIN = Role.ADMIN.name();

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/register", "/login", "/token", "/refresh").permitAll()
//                .requestMatchers("/api-docs/**", "/swagger-ui/**").permitAll()
//                .requestMatchers("/admin/**").hasRole(ADMIN)
//                .requestMatchers("/restaurants/dish/*").hasRole(ADMIN)
//                .requestMatchers("/restaurants/setTime").hasRole(ADMIN)
//                .requestMatchers(HttpMethod.POST, "/restaurants").hasRole(ADMIN)
                .anyRequest().authenticated()
                .and().addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
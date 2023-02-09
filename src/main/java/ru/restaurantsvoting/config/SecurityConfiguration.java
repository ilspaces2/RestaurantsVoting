package ru.restaurantsvoting.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.restaurantsvoting.handler.FilterExceptionHandler;
import ru.restaurantsvoting.security.jwt.JwtFilter;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
//https://stackoverflow.com/questions/72493425/548473
public class SecurityConfiguration {

    private static final String ADMIN = Role.ADMIN.name();

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtFilter jwtFilter;
    private final FilterExceptionHandler filterExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/register", "/login", "/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/restaurants/dishes/*", "/admin/**", "/restaurants/time").hasRole(ADMIN)
                .requestMatchers(HttpMethod.POST, "/restaurants").hasRole(ADMIN)
                .anyRequest().authenticated()
                .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterExceptionHandler, JwtFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
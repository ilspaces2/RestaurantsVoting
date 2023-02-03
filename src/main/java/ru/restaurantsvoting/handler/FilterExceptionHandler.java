package ru.restaurantsvoting.handler;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Перехватчик ошибок у фильтров.
 * В данном случае хотел перехватить ошибки
 * валидации токена из фильтра JwtFilter.
 */
@Component
@Slf4j
public class FilterExceptionHandler extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | IOException e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
            log.error("JwtException: {}", e.getMessage());
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(ex.getMessage());
    }
}
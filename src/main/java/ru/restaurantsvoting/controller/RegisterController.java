package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.mapper.UserMapper;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;

import java.net.URI;

@Tag(name = "Register", description = "Registration in the application")
@RestController
@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    private final UserMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
        User created = userService.save(mapper.toModel(userDto));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/register").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

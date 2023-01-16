package ru.restaurantsvoting.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.dto.UserDTO;
import ru.restaurantsvoting.mapper.UserMapper;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;

import java.net.URI;

@RestController
@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RegisterController {

    private final UserService userService;

    private final UserMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserDTO userDTO) {
        User created = userService.save(mapper.toModel(userDTO));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/register").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

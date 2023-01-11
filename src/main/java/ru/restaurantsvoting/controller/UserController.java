package ru.restaurantsvoting.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;
import ru.restaurantsvoting.dto.UserDTO;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> save(@RequestBody UserDTO userDTO) {
        User created = userService.save(userDTO);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return userService.findAll();
    }
}

package ru.restaurantsvoting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public List<User> findAll() {
        return userService.findAll();
    }
}

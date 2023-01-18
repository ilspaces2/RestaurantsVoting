package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;

import java.util.List;


@Tag(name = "Admin", description = "The Admin API")
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(summary = "Gets all users")
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @Operation(summary = "Get user")
    @GetMapping("{id}")
    public User get(@PathVariable int id) {
        return userService.findById(id);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @Operation(summary = "Update user")
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User update(@RequestBody @Valid UserDto userDTO, @PathVariable int id) {
        return userService.update(userDTO, id);
    }
}

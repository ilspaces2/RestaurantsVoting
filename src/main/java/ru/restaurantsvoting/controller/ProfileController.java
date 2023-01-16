package ru.restaurantsvoting.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.dto.UserDTO;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.security.AuthUser;
import ru.restaurantsvoting.service.UserService;

@RestController
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        userService.delete(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User update(@RequestBody @Valid UserDTO userDTO, @AuthenticationPrincipal AuthUser authUser) {
        return userService.update(userDTO, authUser.id());
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}

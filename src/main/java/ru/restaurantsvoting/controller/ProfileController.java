package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.security.AuthUser;
import ru.restaurantsvoting.service.UserService;

@Tag(name = "Profile", description = "The User API")
@RestController
@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "Delete profile")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        userService.delete(authUser.getId());
    }

    @Operation(summary = "Update profile")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User update(@RequestBody @Valid UserDto userDTO, @AuthenticationPrincipal AuthUser authUser) {
        return userService.update(userDTO, authUser.getId());
    }

    @Operation(summary = "Get profile")
    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return userService.findById(authUser.getId());
    }
}

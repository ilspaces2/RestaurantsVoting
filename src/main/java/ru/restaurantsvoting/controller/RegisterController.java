package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @ApiResponse(
            responseCode = "422",
            description = "Validate error",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Unprocessable Entity",
                      "status": 422,
                      "detail": "Invalid request content.",
                      "instance": "/register",
                      "invalid_params": {
                        "email": "должно иметь формат адреса электронной почты"
                      }
                    }
                    """)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
        User created = userService.save(mapper.toModel(userDto));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/register").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}

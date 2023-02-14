package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.security.jwt.JwtRequestLogin;
import ru.restaurantsvoting.security.jwt.JwtResponse;
import ru.restaurantsvoting.service.AuthService;

@Tag(name = "Authentication", description = "Authentication in the application")
@RestController
@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema =
            @Schema(example = """
                    {
                      "email": "string",
                      "password": "string"
                    }
                    """)))
    @ApiResponse(
            responseCode = "200",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "string",
                      "accessToken": "string"
                    }
                    """)))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody JwtRequestLogin authRequest) {
        return authService.login(authRequest);
    }
}

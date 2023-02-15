package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "400", description = "Token error", content = @Content)})
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
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User update(@RequestBody @Valid UserDto userDTO, @AuthenticationPrincipal AuthUser authUser) {
        return userService.update(userDTO, authUser.getId());
    }

    @Operation(summary = "Get profile")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return userService.findById(authUser.getId());
    }
}

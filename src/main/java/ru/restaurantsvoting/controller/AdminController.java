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
import org.springframework.web.bind.annotation.*;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.service.UserService;

import java.util.List;

@Tag(name = "Admin", description = "The Admin API")
@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "400", description = "Token error", content = @Content)})
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(summary = "Gets all users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        return userService.findAll();
    }

    @Operation(summary = "Get user")
    @ApiResponse(
            responseCode = "404",
            description = "Not found user",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Not Found",
                      "status": 404,
                      "detail": "User with id = 12 not found",
                      "instance": "/admin/12"
                    }
                    """)))
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable int id) {
        return userService.findById(id);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(
            responseCode = "404",
            description = "Not found user",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Not Found",
                      "status": 404,
                      "detail": "User with id = 12 not found",
                      "instance": "/admin/12"
                    }
                    """)))
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
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
                            """))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found user",
                    content = @Content(examples =
                    @ExampleObject(value = """
                            {
                              "type": "about:blank",
                              "title": "Not Found",
                              "status": 404,
                              "detail": "User with id = 12 not found",
                              "instance": "/admin/12"
                            }
                            """)))
    })
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User update(@RequestBody @Valid UserDto userDTO, @PathVariable int id) {
        return userService.update(userDTO, id);
    }
}

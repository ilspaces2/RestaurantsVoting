package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.dto.RestaurantDto;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.security.AuthUser;
import ru.restaurantsvoting.service.RestaurantService;
import ru.restaurantsvoting.validate.ValidateList;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
        @ApiResponse(responseCode = "400", description = "Token error", content = @Content)})
@Tag(name = "Restaurant", description = "The Restaurant API")
@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

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
                      "instance": "/restaurants",
                      "invalid_params": {
                        "name": "размер должен находиться в диапазоне от 2 до 120"
                      }
                    }
                    """)))
    @ApiResponse(
            responseCode = "409",
            description = "Already exists",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Conflict",
                      "status": 409,
                      "detail": "Restaurant already exists with name 'kfc'",
                      "instance": "/restaurants"
                    }
                    """)))
    @Operation(summary = "Add restaurant", description = "This is for admin")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> save(@Valid @RequestBody RestaurantDto restaurantDto) {
        Restaurant created = restaurantService.save(restaurantDto);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema =
            @Schema(example = """
                    [
                      {
                        "name": "string",
                        "price": 1
                      }
                    ]
                    """)))
    @ApiResponse(
            responseCode = "404",
            description = "Not found restaurant",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Not Found",
                      "status": 404,
                      "detail": "Restaurant 'kf' not found",
                      "instance": "/restaurants/dishes/kf"
                    }
                    """)))
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
                      "instance": "/restaurants/dishes/kfc",
                      "invalid_params": {
                        "list[0].price": "должно находиться в диапазоне от 1 до 5000"
                        }
                    }
                    """)))
    @Operation(summary = "Add dishes to restaurant", description = "This is for admin")
    @PutMapping(value = "dishes/{restaurantName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Restaurant addDish(@PathVariable String restaurantName, @RequestBody @Valid ValidateList<Dish> dishes) {
        return restaurantService.addDish(restaurantName, dishes.getList());
    }

    @ApiResponse(
            responseCode = "404",
            description = "Not found restaurant",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Not Found",
                      "status": 404,
                      "detail": "Restaurant 'kf' not found",
                      "instance": "/restaurants/dishes/kf"
                    }
                    """)))
    @Operation(summary = "Vote to restaurant")
    @PutMapping("vote/{restaurantName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String vote(@PathVariable String restaurantName, @AuthenticationPrincipal AuthUser authUser) {
        return restaurantService.vote(restaurantName, authUser.getId());
    }

    @Operation(summary = "Get all restaurants")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> findAll() {
        return restaurantService.findAll();
    }

    @ApiResponse(
            responseCode = "404",
            description = "Not found restaurant",
            content = @Content(examples =
            @ExampleObject(value = """
                    {
                      "type": "about:blank",
                      "title": "Not Found",
                      "status": 404,
                      "detail": "Restaurant id: '12' not found",
                      "instance": "/restaurants/12"
                    }
                    """)))
    @Operation(summary = "Get restaurant by id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant get(@PathVariable int id) {
        return restaurantService.get(id);
    }

    /*
    Add pattern because Swagger UI throw error validation.
    Swagger expected pattern @date-time, but actual value is time.
     */
    @Operation(summary = "Set vote time", description = "This is for admin")
    @PutMapping("time")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setTime(@Schema(pattern = "^(?:2[0-3]|[0-1][0-9]):[0-5][0-9]$",
            defaultValue = "24-hour time, 11:00",
            example = "23:15") @RequestParam LocalTime time) {
        restaurantService.setTime(time);
    }
}

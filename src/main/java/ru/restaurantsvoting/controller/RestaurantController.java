package ru.restaurantsvoting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@Tag(name = "Restaurant", description = "The Restaurant API")
@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "Add restaurant", description = "This is for admin")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> save(@Valid @RequestBody RestaurantDto restaurantDto) {
        Restaurant created = restaurantService.save(restaurantDto);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Add dish to restaurant", description = "This is for admin")
    @PutMapping(value = "dish/{restaurantName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Restaurant addDish(@PathVariable String restaurantName, @RequestBody @Valid ValidateList<Dish> dishes) {
        return restaurantService.addDish(restaurantName, dishes.getList());
    }

    @Operation(summary = "Vote to restaurant")
    @PutMapping("vote/{restaurantName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String vote(@PathVariable String restaurantName, @AuthenticationPrincipal AuthUser authUser) {
        return restaurantService.vote(restaurantName, authUser.getUser());
    }

    @Operation(summary = "Get all restaurants")
    @GetMapping
    public List<Restaurant> findAll() {
        return restaurantService.findAll();
    }

    @Operation(summary = "Get restaurant")
    @GetMapping("{restaurantName}")
    public Restaurant get(@PathVariable String restaurantName) {
        return restaurantService.get(restaurantName);
    }

    /*
    Add pattern because Swagger UI throw error validation.
    Swagger expected pattern @date-time, but actual value is time.
     */
    @Operation(summary = "Set vote time", description = "This is for admin")
    @GetMapping("setTime")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setTime(@Schema(pattern = "^(?:2[0-3]|[0-1][0-9]):[0-5][0-9]$",
            defaultValue = "24-hour time, 11:00",
            example = "23:15") @RequestParam LocalTime time) {
        restaurantService.setTime(time);
    }
}

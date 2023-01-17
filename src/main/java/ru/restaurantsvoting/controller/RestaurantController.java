package ru.restaurantsvoting.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.dto.RestaurantDTO;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.security.AuthUser;
import ru.restaurantsvoting.service.RestaurantService;
import ru.restaurantsvoting.validate.ValidateList;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> save(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        Restaurant created = restaurantService.save(restaurantDTO);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "dish/{restaurantName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Restaurant addDish(@PathVariable String restaurantName, @RequestBody @Valid ValidateList<Dish> dishes) {
        return restaurantService.addDish(restaurantName, dishes.getList());
    }

    @PutMapping("vote/{restaurantName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String vote(@PathVariable String restaurantName, @AuthenticationPrincipal AuthUser authUser) {
        return restaurantService.vote(restaurantName, authUser.getUser());
    }

    @GetMapping
    public List<Restaurant> findAll() {
        return restaurantService.findAll();
    }

    @GetMapping("{restaurantName}")
    public Restaurant get(@PathVariable String restaurantName) {
        return restaurantService.get(restaurantName);
    }

    @GetMapping("setTime")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setTime(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        restaurantService.setTime(time);
    }
}

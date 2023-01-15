package ru.restaurantsvoting.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurantsvoting.dto.RestaurantDTO;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.repository.UserRepository;
import ru.restaurantsvoting.service.RestaurantService;
import ru.restaurantsvoting.validate.ValidateList;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final UserRepository userRepository;

    public RestaurantController(RestaurantService restaurantService, UserRepository userRepository) {
        this.restaurantService = restaurantService;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> save(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        Restaurant created = restaurantService.save(restaurantDTO);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/restaurants").build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/dish/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Restaurant addDish(@PathVariable String name, @RequestBody @Valid ValidateList<Dish> dishes) {
        return restaurantService.addDish(name, dishes.getList());
    }

    @PutMapping("/vote/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void vote(@PathVariable String name) {
        restaurantService.vote(name, userRepository.getReferenceById(2));
    }

    @GetMapping
    public List<Restaurant> findAll() {
        return restaurantService.findAll();
    }

    @GetMapping("/{name}")
    public Restaurant get(@PathVariable String name) {
        return restaurantService.get(name);
    }
}
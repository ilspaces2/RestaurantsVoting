package ru.restaurantsvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantsvoting.dto.RestaurantDTO;
import ru.restaurantsvoting.exception.AlreadyVotedException;
import ru.restaurantsvoting.mapper.RestaurantMapper;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.DishRepository;
import ru.restaurantsvoting.repository.RestaurantRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final DishRepository dishRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.dishRepository = dishRepository;
    }

    public Restaurant save(RestaurantDTO restaurantDTO) {
        log.info("Add restaurant : {}", restaurantDTO.getName());
        return restaurantRepository.save(restaurantMapper.toModel(restaurantDTO));
    }

    public List<Restaurant> findAll() {
        log.info("Get restaurants");
        return restaurantRepository.findAll();
    }

    @Transactional
    public void vote(String restaurantName, User user) {
        get(restaurantName);
        if (user.isVoted() && LocalTime.now().isAfter(LocalTime.of(11, 0, 0))) {
            throw new AlreadyVotedException(String.format("User %s already voted", user.getName()));
        }
        if (user.isVoted() && LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            log.info("User {} cancel vote of {}", user.getName(), restaurantName);
            user.setVoted(false);
            restaurantRepository.cancelVote(restaurantName);
        }
        if (!user.isVoted()) {
            log.info("User {} do vote of {}", user.getName(), restaurantName);
            user.setVoted(true);
            restaurantRepository.doVote(restaurantName);
        }
    }

    @Transactional
    public Restaurant addDish(String restaurantName, List<Dish> dishes) {
        Restaurant restaurant = get(restaurantName);
        log.info("Add {} to {}", dishes, restaurantName);
        dishRepository.saveAll(dishes);
        dishes.forEach(restaurant::addDish);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant get(String restaurantName) {
        return restaurantRepository.findByName(restaurantName).orElseThrow(
                () -> new NoSuchElementException(String.format("Restaurant %s not found", restaurantName))
        );
    }
}

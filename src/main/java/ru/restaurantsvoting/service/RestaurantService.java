package ru.restaurantsvoting.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantsvoting.dto.RestaurantDTO;
import ru.restaurantsvoting.exception.AlreadyExistsException;
import ru.restaurantsvoting.mapper.RestaurantMapper;
import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.DishRepository;
import ru.restaurantsvoting.repository.RestaurantRepository;
import ru.restaurantsvoting.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final DishRepository dishRepository;

    private final UserRepository userRepository;

    @Setter
    private LocalTime time = LocalTime.of(11, 0, 0);

    public Restaurant save(RestaurantDTO restaurantDTO) {
        if (restaurantRepository.findByName(restaurantDTO.getName()).isPresent()) {
            throw new AlreadyExistsException(
                    String.format("Restaurant already exists with name '%s'", restaurantDTO.getName())
            );
        }
        log.info("Add restaurant '{}'", restaurantDTO.getName());
        return restaurantRepository.save(restaurantMapper.toModel(restaurantDTO));
    }

    public List<Restaurant> findAll() {
        log.info("Get restaurants");
        return restaurantRepository.findAll();
    }

    @Transactional
    public String vote(String restaurantName, User user) {
        get(restaurantName);
        if (user.isVoted() && LocalTime.now().isAfter(time)) {
            log.info("User '{}' already voted for '{}'", user.getName(), restaurantName);
            return "Already voted";
        } else if (user.isVoted() && LocalTime.now().isBefore(time)) {
            user.setVoted(false);
            userRepository.save(user);
            restaurantRepository.cancelVote(restaurantName);
            log.info("User '{}' canceled vote for '{}'", user.getName(), restaurantName);
            return "Canceled vote";
        } else {
            user.setVoted(true);
            userRepository.save(user);
            restaurantRepository.doVote(restaurantName);
            log.info("User '{}' voted for '{}'", user.getName(), restaurantName);
            return "Voted";
        }
    }

    @Transactional
    public Restaurant addDish(String restaurantName, List<Dish> dishes) {
        Restaurant restaurant = get(restaurantName);
        dishRepository.saveAll(dishes);
        dishes.forEach(restaurant::addDish);
        log.info("Add '{}' to '{}'", dishes, restaurantName);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant get(String restaurantName) {
        return restaurantRepository.findByName(restaurantName).orElseThrow(
                () -> new NoSuchElementException(String.format("Restaurant '%s' not found", restaurantName))
        );
    }
}

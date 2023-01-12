package ru.restaurantsvoting.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.restaurantsvoting.model.Restaurant;
import ru.restaurantsvoting.model.User;
import ru.restaurantsvoting.repository.RestaurantRepository;
import ru.restaurantsvoting.repository.UserRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant save(Restaurant restaurant) {
        log.info("Add restaurant : {}", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findAll() {
        log.info("Get restaurants");
        return restaurantRepository.findAll();
    }

    public boolean vote(String restaurantName, User user) {
        if (user.isVoted() && LocalTime.now().isAfter(LocalTime.of(11, 0, 0))) {
            log.info("User {} already voted", user.getName());
            return false;
        }
        if (user.isVoted() && LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            log.info("User {} cancel vote of {}", user.getName(), restaurantName);
            user.setVoted(false);
            restaurantRepository.cancelVote(restaurantName);
            userRepository.save(user);
            return true;
        }
        if (!user.isVoted()) {
            log.info("User {} do vote of {}", user.getName(), restaurantName);
            user.setVoted(true);
            restaurantRepository.doVote(restaurantName);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}

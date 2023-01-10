package ru.restaurantsvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restaurantsvoting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}

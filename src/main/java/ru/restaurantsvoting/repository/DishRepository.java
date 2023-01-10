package ru.restaurantsvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restaurantsvoting.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}

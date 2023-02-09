package ru.restaurantsvoting;

import ru.restaurantsvoting.model.Dish;
import ru.restaurantsvoting.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantTestData {

    public static Restaurant getRestaurantOne() {
        return new Restaurant(1, "macdonalds", List.of(new Dish(1, "beer", 10.5)), 1);
    }

    public static Restaurant getRestaurantTwo() {
        return new Restaurant(2, "kfc", List.of(), 0);
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(3, "Bar", List.of(), 0);
    }

    public static List<Restaurant> getRestaurants() {
        return List.of(getRestaurantOne(), getRestaurantTwo());
    }
}

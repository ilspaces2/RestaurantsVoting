package ru.restaurantsvoting.mapper;

import org.mapstruct.Mapper;
import ru.restaurantsvoting.dto.RestaurantDTO;
import ru.restaurantsvoting.model.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Restaurant toModel(RestaurantDTO RestaurantTo);

    RestaurantDTO toDto(Restaurant restaurant);
}

package ru.restaurantsvoting.mapper;

import org.mapstruct.Mapper;
import ru.restaurantsvoting.dto.RestaurantDto;
import ru.restaurantsvoting.model.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Restaurant toModel(RestaurantDto RestaurantTo);
}

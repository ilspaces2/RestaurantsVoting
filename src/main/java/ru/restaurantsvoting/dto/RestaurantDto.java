package ru.restaurantsvoting.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.restaurantsvoting.model.NamedEntity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class RestaurantDto extends NamedEntity {

    public RestaurantDto(String name) {
        super(name);
    }
}

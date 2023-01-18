package ru.restaurantsvoting.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.restaurantsvoting.model.NamedEntity;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantDto extends NamedEntity {
}

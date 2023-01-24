package ru.restaurantsvoting.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.restaurantsvoting.model.NamedEntity;

@Value
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class RestaurantDto extends NamedEntity {
}

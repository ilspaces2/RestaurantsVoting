package ru.restaurantsvoting.dto;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends NamedDTO {

    String email;

    String password;
}

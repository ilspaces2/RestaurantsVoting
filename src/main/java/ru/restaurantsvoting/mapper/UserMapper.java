package ru.restaurantsvoting.mapper;

import org.mapstruct.Mapper;
import ru.restaurantsvoting.dto.UserDTO;
import ru.restaurantsvoting.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserDTO userTo);
}

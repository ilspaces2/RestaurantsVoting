package ru.restaurantsvoting.mapper;

import org.mapstruct.Mapper;
import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserDto userTo);
}

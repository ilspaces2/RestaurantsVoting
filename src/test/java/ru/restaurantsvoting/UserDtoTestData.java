package ru.restaurantsvoting;

import ru.restaurantsvoting.dto.UserDto;

import static ru.restaurantsvoting.UserTestData.*;

public class UserDtoTestData {


    public static UserDto getUserDto() {
        return new UserDto(
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }

    public static UserDto getNewUserDto() {
        return new UserDto(
                getNewUser().getName(),
                getNewUser().getEmail(),
                getNewUser().getPassword());
    }

    public static UserDto getUpdatedUserDto() {
        return new UserDto(
                getUpdatedUser().getName(),
                getUpdatedUser().getEmail(),
                getUpdatedUser().getPassword());
    }

    public static UserDto getBadUserDto() {
        return new UserDto(
                getUpdatedUser().getName(),
                "bad.com",
                "bad");
    }
}

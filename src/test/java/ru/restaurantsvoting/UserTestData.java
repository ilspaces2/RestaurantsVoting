package ru.restaurantsvoting;

import ru.restaurantsvoting.dto.UserDto;
import ru.restaurantsvoting.model.Role;
import ru.restaurantsvoting.model.User;

import java.util.List;
import java.util.Set;

public class UserTestData {

    public static final int ADMIN_ID = 1;

    public static final int USER_ID = 2;

    public static final int BAD_ID = Integer.MAX_VALUE;


    public static final User admin = new User(
            ADMIN_ID,
            "admin",
            "admin@admin.com",
            "admin",
            Set.of(Role.ADMIN));

    public static final User user = new User(
            USER_ID,
            "user1",
            "user@user.com",
            "user1",
            Set.of(Role.USER));

    public static UserDto getUserDto() {
        return new UserDto(
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }

    public static User getNewUser() {
        return new User(
                null,
                "new_user",
                "new@new.com",
                "new_user",
                null);
    }

    public static UserDto getNewUserDto() {
        return new UserDto(
                getNewUser().getName(),
                getNewUser().getEmail(),
                getNewUser().getPassword());
    }

    public static User getUpdatedUser() {
        return new User(
                USER_ID,
                "update_user",
                "update@update.com",
                "update",
                user.getRoles());
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

    public static List<User> getUsers() {
        return List.of(admin, user);
    }
}

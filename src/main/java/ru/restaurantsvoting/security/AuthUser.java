package ru.restaurantsvoting.security;

import lombok.Getter;
import ru.restaurantsvoting.model.User;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final int id;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.id = user.getId();
    }
}
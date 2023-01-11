package ru.restaurantsvoting.model;

public enum Role {
    ADMIN,
    USER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}

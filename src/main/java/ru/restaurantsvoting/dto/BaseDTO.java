package ru.restaurantsvoting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public abstract class BaseDTO {

    protected Integer id;

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}

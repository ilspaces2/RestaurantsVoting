package ru.restaurantsvoting.validate;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidateList<E> implements List<E> {
    @Valid
    @Delegate
    private List<E> list = new ArrayList<>();
}

package ru.restaurantsvoting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NamedDTO extends BaseDTO {

    protected String name;

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}

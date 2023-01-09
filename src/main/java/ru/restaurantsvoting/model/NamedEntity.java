package ru.restaurantsvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class NamedEntity extends BasedEntity {

    private String name;

    public NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }
}

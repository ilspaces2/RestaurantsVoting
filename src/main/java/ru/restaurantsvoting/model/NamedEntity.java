package ru.restaurantsvoting.model;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEntity extends BaseEntity {

    protected String name;

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
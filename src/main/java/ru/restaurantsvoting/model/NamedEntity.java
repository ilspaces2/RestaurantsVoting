package ru.restaurantsvoting.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class NamedEntity extends BaseEntity {

    @NotBlank
    @Size(max = 120)
    protected String name;
}
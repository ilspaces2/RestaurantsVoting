package ru.restaurantsvoting.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public abstract class NamedEntity extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 120)
    protected String name;

    public NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public NamedEntity(String name) {
        this.name = name;
    }
}
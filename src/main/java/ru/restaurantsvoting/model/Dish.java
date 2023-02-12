package ru.restaurantsvoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dishes")
@EqualsAndHashCode(callSuper = true)
public class Dish extends NamedEntity {

    @NotNull
    @Range(min = 1, max = 5000)
    private double price;

    public Dish(Integer id, String name, double price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "price=" + price +
                ", name='" + name + '\'' +
                '}';
    }
}

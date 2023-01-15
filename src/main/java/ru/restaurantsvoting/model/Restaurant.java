package ru.restaurantsvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant extends NamedEntity {

    @ManyToMany
    @JoinTable(name = "restaurant_dishes",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<Dish> dishes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int votes;

    public void addDish(Dish dish) {
        dishes.add(dish);
    }
}

package ru.restaurantsvoting.model;

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

    @OneToMany
    @JoinColumn(name = "dishes_id")
    private List<Dish> dishes;

    private int vote;
}

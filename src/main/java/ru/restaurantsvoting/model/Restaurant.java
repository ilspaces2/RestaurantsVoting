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
    @JoinColumn(name = "dish_id")
    private List<Dish> dishes;

    @OneToMany
    @JoinColumn(name = "vote_id")
    private List<Vote> votes;
}

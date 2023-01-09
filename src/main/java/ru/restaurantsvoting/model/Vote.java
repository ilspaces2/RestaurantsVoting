package ru.restaurantsvoting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vote extends NamedEntity{

    private int userId;

    private int countVote;

    private LocalDateTime dateOfVote;
}

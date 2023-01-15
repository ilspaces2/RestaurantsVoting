package ru.restaurantsvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantsvoting.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("update Restaurant set votes=votes+1 where name=:name")
    void doVote(String name);

    @Transactional
    @Modifying
    @Query("update Restaurant set votes=votes-1 where name=:name")
    void cancelVote(String name);

    Optional<Restaurant> findByName(String name);

    @Query("from Restaurant r left join fetch r.dishes")
    List<Restaurant> findAll();
}

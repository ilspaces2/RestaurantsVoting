package ru.restaurantsvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restaurantsvoting.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}

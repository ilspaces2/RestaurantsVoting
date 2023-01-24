package ru.restaurantsvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.restaurantsvoting.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByEmail(String email);
}

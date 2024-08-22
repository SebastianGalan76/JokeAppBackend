package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.FavoriteJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteJokeRepository extends JpaRepository<FavoriteJoke, Long> {
    Optional<FavoriteJoke> findByUserAndJoke(User user, Joke joke);
}

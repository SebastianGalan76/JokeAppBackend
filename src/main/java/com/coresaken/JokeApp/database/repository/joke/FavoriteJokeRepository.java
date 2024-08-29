package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.FavoriteJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteJokeRepository extends JpaRepository<FavoriteJoke, Long> {
    Optional<FavoriteJoke> findByUserAndJoke(User user, Joke joke);
    @Modifying
    @Query("DELETE FROM FavoriteJoke f WHERE f.user.id = :userId AND f.joke.id = :jokeId")
    void deleteByUserIdAndJokeId(@Param("userId") Long userId, @Param("jokeId") Long jokeId);
}

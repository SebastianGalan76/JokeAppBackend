package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r WHERE r.joke = :joke AND ((:user IS NOT NULL AND r.user = :user) OR (:user IS NULL AND r.userIp = :userIp))")
    Optional<Rating> findByJokeAndUserOrUserIp(@Param("joke") Joke joke, @Param("user") User user, @Param("userIp") String userIp);
}

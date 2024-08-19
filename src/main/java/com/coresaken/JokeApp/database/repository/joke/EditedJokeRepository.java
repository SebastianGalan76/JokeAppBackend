package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.joke.EditedJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditedJokeRepository extends JpaRepository<EditedJoke, Long> {
    EditedJoke findByJoke(Joke joke);
}

package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedJokeRepository extends JpaRepository<ReportedJoke, Long> {
    List<ReportedJoke> findByJoke(Joke joke);

    @Modifying
    @Query("DELETE FROM ReportedJoke r WHERE r.joke = :joke")
    void deleteByJoke(@Param("joke") Joke joke);
}

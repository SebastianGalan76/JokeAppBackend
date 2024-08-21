package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedJokeRepository extends JpaRepository<ReportedJoke, Long> {

}

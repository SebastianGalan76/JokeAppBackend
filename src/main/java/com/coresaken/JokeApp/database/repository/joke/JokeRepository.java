package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {
    Page<Joke> findByCategory(Category category, Pageable pageable);
}

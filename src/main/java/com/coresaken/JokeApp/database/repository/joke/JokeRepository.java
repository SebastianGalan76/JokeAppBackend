package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {
    Page<Joke> findByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT * FROM joke ORDER BY random() LIMIT :amount", nativeQuery = true)
    List<Joke> findRandomJokes(@Param("amount") int amount);
}

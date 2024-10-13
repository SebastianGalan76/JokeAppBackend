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

@Repository
public interface JokeRepository extends JpaRepository<Joke, Long> {
    @Query("SELECT j FROM Joke j JOIN j.categories c WHERE c = :category AND j.status = 'ACCEPTED'")
    Page<Joke> findByCategoriesAndAccepted(@Param("category") Category category, Pageable pageable);

    @Query(value = "SELECT * FROM joke WHERE status = 'ACCEPTED' ORDER BY random() LIMIT :amount", nativeQuery = true)
    List<Joke> findRandomJokes(@Param("amount") int amount);

    @Query("SELECT j FROM Joke j WHERE status = 'ACCEPTED' ORDER BY (j.likeAmount - j.dislikeAmount) DESC")
    Page<Joke> findBestJokes(Pageable pageable);

    @Query("SELECT j FROM Joke j WHERE j.status = 'NOT_VERIFIED'")
    Page<Joke> findUnverifiedJokes(Pageable pageable);

    @Query("SELECT j FROM Joke j WHERE j.status = 'ACCEPTED'")
    Page<Joke> findAccepted(Pageable pageable);

    @Query("SELECT c, COUNT(j) FROM Joke j JOIN j.categories c WHERE j.status = 'ACCEPTED' GROUP BY c")
    List<Object[]> countAcceptedJokesByCategory();
}

package com.coresaken.JokeApp.database.repository;

import com.coresaken.JokeApp.database.model.JokeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeListRepository extends JpaRepository<JokeList, Long> {
}

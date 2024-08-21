package com.coresaken.JokeApp.database.repository;

import com.coresaken.JokeApp.database.model.JokeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JokeListRepository extends JpaRepository<JokeList, Long> {
    Optional<JokeList> findByUuid(UUID uuid);
}

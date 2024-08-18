package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.joke.RejectionReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectionReasonRepository extends JpaRepository<RejectionReason, Long> {
}

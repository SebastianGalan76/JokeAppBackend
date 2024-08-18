package com.coresaken.JokeApp.database.repository;

import com.coresaken.JokeApp.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrLogin(String email, String login);
}

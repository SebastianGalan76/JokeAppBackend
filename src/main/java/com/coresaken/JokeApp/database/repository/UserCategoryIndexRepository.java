package com.coresaken.JokeApp.database.repository;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.UserCategoryIndex;
import com.coresaken.JokeApp.database.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCategoryIndexRepository extends JpaRepository<UserCategoryIndex, Long> {
    Optional<UserCategoryIndex> findByUserAndCategory(User user, Category category);
    List<UserCategoryIndex> findByUser(User user);
}

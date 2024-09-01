package com.coresaken.JokeApp.database.repository.joke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.category.FavoriteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteCategoryRepository extends JpaRepository<FavoriteCategory, Long> {
    Optional<FavoriteCategory> findByUserAndCategory(User user, Category category);
    @Modifying
    @Query("DELETE FROM FavoriteCategory f WHERE f.user.id = :userId AND f.category.id = :categoryId")
    void deleteByUserIdAndCategoryIdId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}

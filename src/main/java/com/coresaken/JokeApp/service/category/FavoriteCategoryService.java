package com.coresaken.JokeApp.service.category;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.category.FavoriteCategory;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.FavoriteCategoryRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteCategoryService {
    final UserService userService;

    final CategoryRepository categoryRepository;
    final FavoriteCategoryRepository favoriteCategoryRepository;

    @Transactional
    @Modifying
    public ResponseEntity<Response> toggle(Long id) {
        User user = userService.getLoggedUser();
        if(user == null){
            return ErrorResponse.build(1, "Your session has been expired");
        }

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ErrorResponse.build(2, "There is no category with given id");
        }

        FavoriteCategory savedFavoriteCategory = favoriteCategoryRepository.findByUserAndCategory(user, category).orElse(null);
        if(savedFavoriteCategory != null){
            favoriteCategoryRepository.deleteByUserIdAndCategoryIdId(user.getId(), category.getId());
        }
        else{
            FavoriteCategory favoriteCategory = new FavoriteCategory();
            favoriteCategory.setUser(user);
            favoriteCategory.setCategory(category);
            favoriteCategoryRepository.save(favoriteCategory);
        }

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }
}

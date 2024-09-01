package com.coresaken.JokeApp.service.category;

import com.coresaken.JokeApp.data.response.CategoryDto;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.category.FavoriteCategory;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    final CategoryRepository categoryRepository;

    final UserService userService;

    public List<CategoryDto> getCategories() {
        Optional<User> optionalUser = Optional.ofNullable(userService.getLoggedUser());

        List<Long> favoriteCategoryIds = optionalUser
                .map(user -> user.getFavoriteCategories().stream()
                        .map(FavoriteCategory::getId)
                        .toList())
                .orElseGet(List::of);

        return categoryRepository.findAll().stream()
                .map(category -> mapToCategoryDto(category, favoriteCategoryIds))
                .toList();
    }

    private CategoryDto mapToCategoryDto(Category category, List<Long> favoriteCategoryIds) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getJokeAmount(),
                favoriteCategoryIds.contains(category.getId())
        );
    }
}

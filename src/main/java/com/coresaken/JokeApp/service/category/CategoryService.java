package com.coresaken.JokeApp.service.category;

import com.coresaken.JokeApp.data.response.CategoryDto;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.UserCategoryIndex;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.category.FavoriteCategory;
import com.coresaken.JokeApp.database.repository.UserCategoryIndexRepository;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    final CategoryRepository categoryRepository;
    final JokeRepository jokeRepository;
    final UserCategoryIndexRepository userCategoryIndexRepository;

    final UserService userService;

    public List<CategoryDto> getCategories() {
        Optional<User> optionalUser = Optional.ofNullable(userService.getLoggedUser());

        List<Long> favoriteCategoryIds = optionalUser
                .map(user -> user.getFavoriteCategories().stream()
                        .map(FavoriteCategory::getId)
                        .toList())
                .orElseGet(List::of);

        List<Category> categories = categoryRepository.findAll();
        List<UserCategoryIndex> userCategoryIndices = new ArrayList<>();
        if(optionalUser.isPresent()){
            userCategoryIndices = userCategoryIndexRepository.findByUser(optionalUser.get());
        }

        Map<Long, Integer> categoryIndexMap = userCategoryIndices.stream()
                .collect(Collectors.toMap(uci -> uci.getCategory().getId(), UserCategoryIndex::getIndex));

        return categories.stream()
                .map(category -> {
                    int userIndex = optionalUser.isPresent()
                            ? categoryIndexMap.getOrDefault(category.getId(), 0)
                            : -1;
                    return mapToCategoryDto(category, favoriteCategoryIds, userIndex);
                })
                .toList();
    }

    @Transactional
    public void updateJokeAmounts() {
        List<Category> categories = categoryRepository.findAll();
        categories.forEach(category -> {
            category.setJokeAmount(0);
            categoryRepository.save(category);
        });


        List<Object[]> results = jokeRepository.countAcceptedJokesByCategory();
        for (Object[] result : results) {
            Category category = (Category) result[0];
            Long jokeCount = (Long) result[1];
            category.setJokeAmount(jokeCount.intValue());
            categoryRepository.save(category);
        }
    }

    private CategoryDto mapToCategoryDto(Category category, List<Long> favoriteCategoryIds, int userIndex) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getUrl(),
                category.getJokeAmount(),
                favoriteCategoryIds.contains(category.getId()),
                userIndex
        );
    }
}

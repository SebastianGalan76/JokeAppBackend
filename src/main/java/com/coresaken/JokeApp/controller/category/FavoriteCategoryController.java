package com.coresaken.JokeApp.controller.category;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.category.FavoriteCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteCategoryController {
    final FavoriteCategoryService favoriteCategoryService;

    @PostMapping("/category/{id}/favorite")
    public ResponseEntity<Response> toggleFavorite(@PathVariable("id") Long id){
        return favoriteCategoryService.toggle(id);
    }
}

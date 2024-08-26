package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.CategoryDto;
import com.coresaken.JokeApp.service.joke.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    final CategoryService categoryService;


    @GetMapping("/categories")
    public List<CategoryDto> getCategories(){
        return categoryService.getCategories();
    }
}

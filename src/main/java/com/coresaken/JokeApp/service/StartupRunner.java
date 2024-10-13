package com.coresaken.JokeApp.service;

import com.coresaken.JokeApp.service.category.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final CategoryService categoryService;

    public StartupRunner(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryService.updateJokeAmounts();
    }
}
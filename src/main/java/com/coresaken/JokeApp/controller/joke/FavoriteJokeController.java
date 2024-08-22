package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.FavoriteJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteJokeController {
    final FavoriteJokeService favoriteJokeService;

    @PostMapping("/joke/favorite/{id}")
    public ResponseEntity<Response> toggleFavoriteJoke(@PathVariable("id") Long id){
        return favoriteJokeService.toggle(id);
    }
}

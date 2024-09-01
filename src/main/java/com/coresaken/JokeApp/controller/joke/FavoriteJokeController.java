package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.service.joke.FavoriteJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FavoriteJokeController {
    final FavoriteJokeService favoriteJokeService;

    @GetMapping("/favorite")
    public ResponseEntity<PageResponse<JokeDto>> getFavoriteJokes(@RequestParam("page") int page){
        return favoriteJokeService.getJokes(page);
    }

    @PostMapping("/joke/favorite/{id}")
    public ResponseEntity<Response> toggleFavoriteJoke(@PathVariable("id") Long id){
        return favoriteJokeService.toggle(id);
    }
}

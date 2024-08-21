package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.service.joke.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JokeController {
    final JokeService jokeService;

    @GetMapping("/joke/{id}")
    public ResponseEntity<Joke> getJokeById(@PathVariable("id") Long id){
        return jokeService.getJokeById(id);
    }

    @GetMapping("/joke/category/{id}/{page}")
    public ResponseEntity<PageResponse<Joke>> getJokesByCategory(@PathVariable("id") Long id, @PathVariable("page") int page){
        return jokeService.getJokesByCategory(id, page);
    }
}

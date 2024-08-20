package com.coresaken.JokeApp.controller.list;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.jokelist.JokeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JokeListController {
    final JokeListService jokeListService;

    @PostMapping("/joke-list/{id}/{jokeId}")
    public ResponseEntity<Response> addJokeToList(@PathVariable("id") Long id, @PathVariable("jokeId") Long jokeId){
        return jokeListService.addJokeToList(id, jokeId);
    }

    @DeleteMapping("/joke-list/{id}/{jokeId}")
    public ResponseEntity<Response> deleteJokeFromList(@PathVariable("id") Long id, @PathVariable("jokeId") Long jokeId){
        return jokeListService.deleteJokeFromList(id, jokeId);
    }
}

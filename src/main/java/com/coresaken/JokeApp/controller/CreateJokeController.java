package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.CreateJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateJokeController {
    final CreateJokeService createJokeService;

    @PostMapping("/joke/create")
    public ResponseEntity<Response> create(@RequestBody JokeDto jokeDto){
        return createJokeService.create(jokeDto);
    }
}

package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.EditJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EditJokeController {
    final EditJokeService editJokeService;

    @PutMapping("/joke/{id}")
    public ResponseEntity<Response> editJoke(@PathVariable("id") Long id, @RequestBody JokeDto jokeDto){
        return editJokeService.editJoke(id, jokeDto);
    }
}

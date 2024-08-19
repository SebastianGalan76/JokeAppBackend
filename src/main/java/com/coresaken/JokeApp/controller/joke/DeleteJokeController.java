package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.DeleteJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteJokeController {
    final DeleteJokeService deleteJokeService;

    @DeleteMapping("/joke/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id){
        return deleteJokeService.delete(id);
    }
}

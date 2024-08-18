package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.JokeStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JokeStatusController {
    final JokeStatusService jokeStatusService;

    @PostMapping("/joke/{id}/accept")
    public ResponseEntity<Response> accept(@PathVariable("id") Long id){
        return jokeStatusService.accept(id);
    }

    @PostMapping("/joke/{id}/reject")
    public ResponseEntity<Response> reject(@PathVariable("id") Long id, @RequestBody String reason){
        return jokeStatusService.reject(id, reason);
    }
}

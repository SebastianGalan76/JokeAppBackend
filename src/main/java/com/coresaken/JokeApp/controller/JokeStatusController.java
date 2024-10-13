package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.JokeStatusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

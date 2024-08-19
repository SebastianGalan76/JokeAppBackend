package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.RateJokeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RateJokeController {
    public RateJokeService rateJokeService;

    @PostMapping("/joke/{id}/like")
    public ResponseEntity<Response> like(@PathVariable("id") Long id, HttpServletRequest request){
        return rateJokeService.like(id, request);
    }

    @PostMapping("/joke/{id}/dislike")
    public ResponseEntity<Response> dislike(@PathVariable("id") Long id, HttpServletRequest request){
        return rateJokeService.dislike(id, request);
    }
}

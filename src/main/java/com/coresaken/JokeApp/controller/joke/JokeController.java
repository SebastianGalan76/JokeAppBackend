package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.service.joke.JokeService;
import com.sun.tools.jconsole.JConsoleContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JokeController {
    final JokeService jokeService;

    @GetMapping("/jokes")
    public PageResponse<JokeDto> getJokes(@RequestParam int page, HttpServletRequest request){
        return jokeService.getJokes(page, request);
    }
    @GetMapping("/joke/{id}")
    public ResponseEntity<JokeDto> getJokeById(@PathVariable("id") Long id){
        return jokeService.getJokeDtoById(id);
    }

    @GetMapping("/joke/category/{id}/{page}")
    public ResponseEntity<PageResponse<JokeDto>> getJokesByCategory(@PathVariable("id") Long id, @PathVariable("page") int page, HttpServletRequest request){
        System.out.println("S1");
        return jokeService.getJokesByCategory(id, page, request);
    }

    @GetMapping("/joke/random")
    public ResponseContent<List<JokeDto>> getRandomJokes(@RequestParam("amount") int amount, HttpServletRequest request){
        return jokeService.getRandomJokes(amount, request);
    }
}

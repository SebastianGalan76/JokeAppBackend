package com.coresaken.JokeApp.controller.list;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.service.jokelist.JokeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class JokeListController {
    final JokeListService jokeListService;

    @GetMapping("/joke-list/{uuid}")
    public ResponseEntity<JokeList> getJokeListByUuid(@PathVariable("uuid")UUID uuid){
        return jokeListService.getByUuid(uuid);
    }

    @DeleteMapping("/joke-list/{uuid}")
    public ResponseEntity<Response> deleteJokeListByUuid(@PathVariable("uuid")UUID uuid){
        return jokeListService.deleteByUuid(uuid);
    }
}

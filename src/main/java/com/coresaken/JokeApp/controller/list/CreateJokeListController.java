package com.coresaken.JokeApp.controller.list;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.jokelist.CreateJokeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateJokeListController {
    final CreateJokeListService createJokeListService;

    @PostMapping("/joke-list/create")
    public ResponseEntity<Response> create(@RequestBody JokeListDto jokeListDto){
        return createJokeListService.create(jokeListDto);
    }
}

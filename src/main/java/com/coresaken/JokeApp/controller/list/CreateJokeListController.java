package com.coresaken.JokeApp.controller.list;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.service.jokelist.CreateJokeListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateJokeListController {
    final CreateJokeListService createJokeListService;

    @PostMapping("/joke-list")
    public ResponseEntity<ResponseContent<com.coresaken.JokeApp.data.response.JokeListDto>> create(@RequestBody JokeListDto jokeListDto, HttpServletRequest request){
        return createJokeListService.create(jokeListDto, request);
    }
}

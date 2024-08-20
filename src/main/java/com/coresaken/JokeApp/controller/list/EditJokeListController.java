package com.coresaken.JokeApp.controller.list;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.jokelist.EditJokeListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditJokeListController {
    final EditJokeListService editJokeListService;

    @PutMapping("/joke-list/{id}")
    public ResponseEntity<Response> edit(@PathVariable("id") Long id, @RequestBody JokeListDto jokeListDto){
        return editJokeListService.edit(id, jokeListDto);
    }
}

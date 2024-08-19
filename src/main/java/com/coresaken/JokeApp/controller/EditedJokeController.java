package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.editedjoke.EditedJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditedJokeController {
    final EditedJokeService editedJokeService;

    @PostMapping("/editedJoke/{id}/accept")
    public ResponseEntity<Response> acceptChanges(@PathVariable("id") Long editedJokeId){
        return editedJokeService.accept(editedJokeId);
    }

    @PostMapping("/editedJoke/{id}/reject")
    public ResponseEntity<Response> rejectChanges(@PathVariable("id") Long editedJokeId){
        return editedJokeService.reject(editedJokeId);
    }
}

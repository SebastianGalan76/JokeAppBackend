package com.coresaken.JokeApp.controller.joke;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.joke.ReportJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportJokeController {
    final ReportJokeService reportJokeService;

    @PostMapping("/joke/{id}/report")
    public ResponseEntity<Response> reportJoke(@PathVariable("id") Long id, @RequestBody String reason){
        return  reportJokeService.report(id, reason);
    }
}

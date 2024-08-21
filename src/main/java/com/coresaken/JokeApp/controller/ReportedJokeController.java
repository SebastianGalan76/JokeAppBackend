package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.service.ReportedJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportedJokeController {
    final ReportedJokeService reportedJokeService;

    @PostMapping("/reported-joke/{id}/accept")
    public ResponseEntity<Response> acceptReport(@PathVariable("id") Long id){
        return reportedJokeService.accept(id);
    }

    @PostMapping("/reported-joke/{id}/reject")
    public ResponseEntity<Response> rejectReport(@PathVariable("id") Long id){
        return reportedJokeService.reject(id);
    }
}

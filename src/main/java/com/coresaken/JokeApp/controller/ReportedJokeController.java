package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import com.coresaken.JokeApp.service.ReportedJokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReportedJokeController {
    final ReportedJokeService reportedJokeService;

    @GetMapping("/reported-jokes")
    public ResponseEntity<PageResponse<ReportedJoke>> getReportedJokes(@RequestParam("page") int page){
        return reportedJokeService.getReportedJokes(page);
    }

    @PostMapping("/reported-joke/{id}/accept")
    public ResponseEntity<Response> acceptReport(@PathVariable("id") Long id){
        return reportedJokeService.accept(id);
    }

    @PostMapping("/reported-joke/{id}/reject")
    public ResponseEntity<Response> rejectReport(@PathVariable("id") Long id){
        return reportedJokeService.reject(id);
    }
}

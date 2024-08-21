package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.ReportedJokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportJokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final ReportedJokeRepository reportedJokeRepository;

    public ResponseEntity<Response> report(Long id, String reason) {
        if(reason == null || reason.length() < 10){
            return ErrorResponse.build(1, "Reason is too short");
        }

        reason = reason.substring(0, Math.min(512, reason.length()));
        Joke joke = jokeRepository.findById(id).orElse(null);
        if(joke == null){
            return ErrorResponse.build(2, "There is no joke with given ID");
        }

        User user = userService.getLoggedUser();

        ReportedJoke reportedJoke = new ReportedJoke();
        reportedJoke.setJoke(joke);
        reportedJoke.setUser(user);
        reportedJoke.setReason(reason);
        reportedJoke.setReportedAt(LocalDateTime.now());
        reportedJokeRepository.save(reportedJoke);

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }
}

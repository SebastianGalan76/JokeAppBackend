package com.coresaken.JokeApp.service;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.ReportedJokeRepository;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedJokeService {
    final UserService userService;

    final ReportedJokeRepository reportedJokeRepository;
    final JokeRepository jokeRepository;

    public ResponseEntity<PageResponse<ReportedJoke>> getReportedJokes(int page) {
        Pageable pageable = PageRequest.of(page, 15);
        Page<ReportedJoke> jokes = reportedJokeRepository.findAll(pageable);
        PageResponse<ReportedJoke> jokeResponse = new PageResponse<>();

        jokeResponse.setStatus(ResponseStatusEnum.SUCCESS);
        jokeResponse.setContent(jokes);
        return ResponseEntity.ok(jokeResponse);
    }

    public ResponseEntity<Response> accept(Long id) {
        ReportedJoke reportedJoke = reportedJokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> verificationRequirementsResponse = checkRequirements(reportedJoke, user);
        if(verificationRequirementsResponse.getStatusCode() != HttpStatus.OK){
            return verificationRequirementsResponse;
        }

        assert reportedJoke != null;
        jokeRepository.delete(reportedJoke.getJoke());
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    public ResponseEntity<Response> reject(Long id) {
        ReportedJoke reportedJoke = reportedJokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> verificationRequirementsResponse = checkRequirements(reportedJoke, user);
        if(verificationRequirementsResponse.getStatusCode() != HttpStatus.OK){
            return verificationRequirementsResponse;
        }

        assert reportedJoke != null;
        reportedJokeRepository.deleteByJoke(reportedJoke.getJoke());
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    private ResponseEntity<Response> checkRequirements(ReportedJoke reportedJoke, User user) {
        if(reportedJoke == null){
            return ErrorResponse.build(1, "There is no report with given id");
        }

        if(user == null){
            return ErrorResponse.build(2, "Your session has been expired");
        }

       if(!PermissionChecker.hasPermission(user, User.Role.HELPER)){
           return ErrorResponse.build(3, "You don't have required permissions");
       }

        return ResponseEntity.ok().build();
    }
}

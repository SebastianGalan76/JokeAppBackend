package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.RejectionReason;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.RejectionReasonRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JokeStatusService {
    final String DEFAULT_REJECTION_REASON = "The content of the joke is against our rules";

    final UserService userService;

    final JokeRepository jokeRepository;
    final RejectionReasonRepository rejectionReasonRepository;

    @Transactional
    public ResponseEntity<Response> accept(Long id) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> response = checkRequirements(joke, user);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        assert joke != null;
        joke.setStatus(Joke.StatusType.ACCEPTED);
        joke.setRejectionReason(null);
        jokeRepository.save(joke);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Response> reject(Long id, String reason) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> response = checkRequirements(joke, user);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        reason = reason.trim();
        if(reason.isEmpty()){
            reason = DEFAULT_REJECTION_REASON;
        }
        if(reason.length()>512){
            return ErrorResponse.build(4, "Provided reason is too long");
        }

        assert joke != null;
        joke.setStatus(Joke.StatusType.REJECTED);

        RejectionReason rejectionReason = new RejectionReason();
        rejectionReason.setReason(reason);
        rejectionReason.setUser(user);
        rejectionReason.setRejectedAt(LocalDateTime.now());
        rejectionReasonRepository.save(rejectionReason);

        joke.setRejectionReason(rejectionReason);
        jokeRepository.save(joke);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    public void sendToVerification(Long id) {
        Joke joke = jokeRepository.findById(id).orElse(null);

        assert joke != null;
        joke.setStatus(Joke.StatusType.NOT_VERIFIED);
        joke.setRejectionReason(null);

        jokeRepository.save(joke);
    }

    private ResponseEntity<Response> checkRequirements(Joke joke, User user){
        if(joke == null){
            return ErrorResponse.build(1, "There is no joke with given id");
        }

        if(user == null){
            return ErrorResponse.build(2, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }

        if(!PermissionChecker.hasPermission(user, User.Role.HELPER)){
            return ErrorResponse.build(3, "You don't have required permission", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }
}

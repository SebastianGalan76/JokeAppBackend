package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.PermissionChecker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public record CreateJokeService(UserService userService, JokeRepository jokeRepository) {
    public ResponseEntity<Response> create(JokeDto jokeDto) {
        User user = userService.getLoggedUser();

        String content = jokeDto.getContent().trim();
        if(content.length()<30){
            return ErrorResponse.build(1, "Joke's content is too short");
        }

        Joke joke = new Joke();
        joke.setCategory(jokeDto.getCategory());
        joke.setContent(content);
        joke.setCharCount(content.length());

        joke.setUser(user);
        joke.setCreatedAt(LocalDateTime.now());
        joke.setLikeAmount(0);
        joke.setDislikeAmount(0);

        if (PermissionChecker.hasPermission(user, User.Role.HELPER)) {
            joke.setStatus(Joke.StatusType.ACCEPTED);
        } else {
            joke.setStatus(Joke.StatusType.NOT_VERIFIED);
        }

        jokeRepository.save(joke);

        Response response = new Response();
        response.setStatus(ResponseStatusEnum.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

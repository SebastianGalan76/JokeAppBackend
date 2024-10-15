package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.*;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.database.repository.joke.FavoriteJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteJokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final JokeListRepository jokeListRepository;
    final FavoriteJokeRepository favoriteJokeRepository;

    @Transactional
    public ResponseEntity<Response> delete(Long id) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> response = checkRequirements(joke, user);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        assert joke != null;
        if(joke.getStatus()== Joke.StatusType.ACCEPTED){
            Optional.ofNullable(joke.getCategories())
                    .ifPresent(categories -> categories.forEach(category -> category.changeJokeAmount(-1)));
        }

        jokeRepository.delete(joke);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    private ResponseEntity<Response> checkRequirements(Joke joke, User user){
        if(joke == null){
            return ErrorResponse.build(1, "There is no joke with given id");
        }

        if(user == null){
            return ErrorResponse.build(2, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }

        if(!PermissionChecker.hasPermission(user, User.Role.HELPER) || (joke.getUser()!= null && !joke.getUser().equals(user))){
            return ErrorResponse.build(3, "You don't have required permission", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }
}

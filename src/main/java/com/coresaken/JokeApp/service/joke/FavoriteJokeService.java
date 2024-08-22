package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.FavoriteJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.FavoriteJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteJokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final FavoriteJokeRepository favoriteJokeRepository;

    public ResponseEntity<Response> toggle(Long id) {
        User user = userService.getLoggedUser();
        if(user == null){
            return ErrorResponse.build(1, "Your session has been expired");
        }

        Joke joke = jokeRepository.findById(id).orElse(null);
        if(joke == null){
            return ErrorResponse.build(2, "There is no joke with given id");
        }

        FavoriteJoke savedFavoriteJoke = favoriteJokeRepository.findByUserAndJoke(user, joke).orElse(null);
        if(savedFavoriteJoke != null){
            favoriteJokeRepository.delete(savedFavoriteJoke);
        }
        else{
            FavoriteJoke favoriteJoke = new FavoriteJoke();
            favoriteJoke.setUser(user);
            favoriteJoke.setJoke(joke);
            favoriteJokeRepository.save(favoriteJoke);
        }

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }
}

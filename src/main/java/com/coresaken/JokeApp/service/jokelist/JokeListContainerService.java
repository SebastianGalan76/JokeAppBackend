package com.coresaken.JokeApp.service.jokelist;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JokeListContainerService {
    final UserService userService;

    final JokeListRepository jokeListRepository;
    final JokeRepository jokeRepository;

    public ResponseEntity<Response> addJokeToList(Long id, Long jokeId) {
        User user = userService.getLoggedUser();
        JokeList jokeList = jokeListRepository.findById(id).orElse(null);
        Joke joke = jokeRepository.findById(jokeId).orElse(null);

        ResponseEntity<Response> verificationResponse = checkRequirements(user, jokeList, joke);
        if(verificationResponse.getStatusCode() != HttpStatus.OK){
            return verificationResponse;
        }

        assert jokeList != null;
        jokeList.getJokes().add(joke);
        jokeListRepository.save(jokeList);
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    public ResponseEntity<Response> deleteJokeFromList(Long id, Long jokeId) {
        User user = userService.getLoggedUser();
        JokeList jokeList = jokeListRepository.findById(id).orElse(null);
        Joke joke = jokeRepository.findById(jokeId).orElse(null);

        ResponseEntity<Response> verificationResponse = checkRequirements(user, jokeList, joke);
        if(verificationResponse.getStatusCode() != HttpStatus.OK){
            return verificationResponse;
        }

        assert jokeList != null;
        jokeList.getJokes().remove(joke);
        jokeListRepository.save(jokeList);
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    private ResponseEntity<Response> checkRequirements(User user, JokeList jokeList, Joke joke){
        if(user == null){
            return ErrorResponse.build(1, "Twoja sesja wygasła. Zaloguj się ponownie", HttpStatus.UNAUTHORIZED);
        }
        if(jokeList == null){
            return ErrorResponse.build(2, "Nie ma listy dowcipów o podanym identyfikatorze");
        }
        if(joke == null){
            return ErrorResponse.build(3, "Nie ma dowcipu o podanym identyfikatorze");
        }

        if(!jokeList.getUser().equals(user)){
            return ErrorResponse.build(4, "Nie masz uprawnień, aby modyfikować tę listę dowcipów");
        }

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }
}

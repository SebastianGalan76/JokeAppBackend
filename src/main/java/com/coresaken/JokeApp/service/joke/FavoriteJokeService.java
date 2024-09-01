package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.FavoriteJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.FavoriteJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorPageResponse;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteJokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final FavoriteJokeRepository favoriteJokeRepository;

    @Transactional
    @Modifying
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
            favoriteJokeRepository.deleteByUserIdAndJokeId(user.getId(), joke.getId());
        }
        else{
            FavoriteJoke favoriteJoke = new FavoriteJoke();
            favoriteJoke.setUser(user);
            favoriteJoke.setJoke(joke);
            favoriteJokeRepository.save(favoriteJoke);
        }

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    public ResponseEntity<PageResponse<JokeDto>> getJokes(int page) {
        User user = userService.getLoggedUser();
        if(user == null){
            return new ErrorPageResponse<JokeDto>().build(1, "Zaloguj się, aby zobaczyć ulubione dowcipy");
        }

        int size = 15;
        Pageable pageable = PageRequest.of(page, size);

        List<FavoriteJoke> favoriteJokes = user.getFavoriteJokes();

        int start = Math.min((int) pageable.getOffset(), favoriteJokes.size());
        int end = Math.min((start + pageable.getPageSize()), favoriteJokes.size());

        List<JokeDto> subList = favoriteJokes.subList(start, end).stream().map(FavoriteJoke::getJoke).toList().stream().map(joke -> JokeDto.build(user, joke, null)).toList();
        Page<JokeDto> pageOfItems = new PageImpl<>(subList, pageable, favoriteJokes.size());

        PageResponse<JokeDto> jokeResponse = new PageResponse<>();
        jokeResponse.setStatus(ResponseStatusEnum.SUCCESS);
        jokeResponse.setContent(pageOfItems);

        return ResponseEntity.ok(jokeResponse);
    }
}

package com.coresaken.JokeApp.service.joke;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;

    public ResponseEntity<PageResponse<JokeDto>> getJokesByCategory(Long id, int page, HttpServletRequest request) {
        User user = userService.getLoggedUser();

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().body(PageResponse.buildError(1, "There is no category with given ID"));
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<Joke> jokes = jokeRepository.findByCategories(category, pageable);
        Page<JokeDto> jokeDtoPage = jokes.map(joke -> JokeDto.build(user, joke, request.getRemoteAddr()));

        PageResponse<JokeDto> jokeResponse = new PageResponse<>();
        jokeResponse.setStatus(ResponseStatusEnum.SUCCESS);
        jokeResponse.setContent(jokeDtoPage);

        return ResponseEntity.ok(jokeResponse);
    }
    public ResponseEntity<Response> checkJokeContent(String content){
        int contentLength = content.length();
        if(contentLength<10){
            return ErrorResponse.build(1, "Joke's content is too short");
        }
        if(contentLength>5000){
            return ErrorResponse.build(2, "Joke's content is too long");
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<JokeDto> getJokeDtoById(Long id) {
        User user = userService.getLoggedUser();
        Joke joke = jokeRepository.findById(id).orElse(null);

        if(joke == null){
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(JokeDto.build(user, joke, null));
    }

    public PageResponse<JokeDto> getJokes(int page, HttpServletRequest request) {
        User user = userService.getLoggedUser();
        Pageable pageable = PageRequest.of(page, 15, Sort.by("id").descending());

        Page<Joke> jokes = jokeRepository.findAll(pageable);
        Page<JokeDto> jokeDtoPage = jokes.map(joke -> JokeDto.build(user, joke, request.getRemoteAddr()));

        PageResponse<JokeDto> jokeResponse = new PageResponse<>();
        jokeResponse.setStatus(ResponseStatusEnum.SUCCESS);
        jokeResponse.setContent(jokeDtoPage);

        return jokeResponse;
    }

    public ResponseContent<List<JokeDto>> getRandomJokes(int amount, HttpServletRequest request) {
        User user = userService.getLoggedUser();

        List<Joke> jokes = jokeRepository.findRandomJokes(amount);
        List<JokeDto> jokeDtoPage = jokes.stream().map(joke -> JokeDto.build(user, joke, request.getRemoteAddr())).toList();

        ResponseContent<List<JokeDto>> responseContent = new ResponseContent<>();
        responseContent.setStatus(ResponseStatusEnum.SUCCESS);
        responseContent.setContent(jokeDtoPage);

        return responseContent;
    }
}

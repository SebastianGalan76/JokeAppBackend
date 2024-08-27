package com.coresaken.JokeApp.service.joke;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorPageResponse;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;

    public ResponseEntity<PageResponse<JokeDto>> getJokesByCategory(Long id, int page) {
        User user = userService.getLoggedUser();

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return new ErrorPageResponse<JokeDto>().build(1, "There is no category with given ID");
        }

        Pageable pageable = PageRequest.of(page, 15);

        Page<Joke> jokes = jokeRepository.findByCategory(category, pageable);
        Page<JokeDto> jokeDtoPage = jokes.map(joke -> JokeDto.build(user, joke));

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

        return ResponseEntity.ok(JokeDto.build(user, joke));
    }
}

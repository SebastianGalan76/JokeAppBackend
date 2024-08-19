package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateJokeService {
    final UserService userService;
    final JokeService jokeService;
    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;

    public ResponseEntity<Response> create(JokeDto jokeDto) {
        User user = userService.getLoggedUser();

        String content = jokeDto.getContent().trim();
        ResponseEntity<Response> contentVerificationResponse = jokeService.checkJokeContent(content);
        if(contentVerificationResponse.getStatusCode() != HttpStatus.OK){
            return contentVerificationResponse;
        }

        Joke joke = new Joke();
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

        Category category = jokeDto.getCategory();
        if(category != null){
            Category savedCategory = categoryRepository.findById(jokeDto.getCategory().getId()).orElse(null);

            if(savedCategory == null){
                return ErrorResponse.build(3, "There is no category with given ID");
            }

            joke.setCategory(savedCategory);
            savedCategory.changeJokeAmount(1);
        }

        jokeRepository.save(joke);

        Response response = new Response();
        response.setStatus(ResponseStatusEnum.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.database.model.category.Category;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        List<Category> categories = jokeDto.getCategories();
        if(categories != null){
            if(joke.getCategories() == null){
                joke.setCategories(new ArrayList<>());
            }

            for(Category category: categories){
                Category savedCategory = categoryRepository.findById(category.getId()).orElse(null);

                if(savedCategory != null){
                    joke.getCategories().add(savedCategory);

                    if(joke.getStatus()== Joke.StatusType.ACCEPTED){
                        savedCategory.changeJokeAmount(1);
                    }
                }
            }
        }

        Joke.Type type = Joke.Type.valueOf((jokeDto.getType() == null || jokeDto.getType().isBlank()) ? "JOKE" : jokeDto.getType());
        Joke.Kind kind = Joke.Kind.valueOf((jokeDto.getKind() == null || jokeDto.getKind().isBlank()) ? "TRADITIONAL" : jokeDto.getKind());
        joke.setType(type);
        joke.setKind(kind);

        jokeRepository.save(joke);

        Response response = new Response();
        response.setStatus(ResponseStatusEnum.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

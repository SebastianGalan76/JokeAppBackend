package com.coresaken.JokeApp.service.joke;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.JokeResponse;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JokeService {
    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;

    public ResponseEntity<JokeResponse> getJokesByCategory(Long id, int page) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ErrorResponse.buildJokeResponse(1, "There is no category with given ID");
        }

        Pageable pageable = PageRequest.of(page, 15);
        Page<Joke> jokes = jokeRepository.findByCategory(category, pageable);
        JokeResponse jokeResponse = new JokeResponse();
        jokeResponse.setStatus(ResponseStatusEnum.SUCCESS);
        jokeResponse.setContent(jokes);
        return ResponseEntity.ok(jokeResponse);
    }

    public ResponseEntity<Response> checkJokeContent(String content){
        int contentLength = content.length();
        if(contentLength<30){
            return ErrorResponse.build(1, "Joke's content is too short");
        }
        if(contentLength>5000){
            return ErrorResponse.build(2, "Joke's content is too long");
        }

        return ResponseEntity.ok().build();
    }
}

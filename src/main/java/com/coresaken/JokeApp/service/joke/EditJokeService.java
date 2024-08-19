package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.model.joke.EditedJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.EditedJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EditJokeService {
    final UserService userService;
    final JokeService jokeService;

    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;
    final EditedJokeRepository editedJokeRepository;

    @Transactional
    public ResponseEntity<Response> editJoke(Long id, JokeDto jokeDto) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        String content = jokeDto.getContent().trim();
        ResponseEntity<Response> verificationRequirementsResponse = checkRequirements(joke, user, content);
        if(verificationRequirementsResponse.getStatusCode() != HttpStatus.OK){
            return verificationRequirementsResponse;
        }

        if(PermissionChecker.hasPermission(user, User.Role.HELPER)){
            return editByStaff(joke, jokeDto.getCategory(), content);
        }
        else{
            return editByUser(user, joke, jokeDto.getCategory(), content);
        }
    }

    private ResponseEntity<Response> editByUser(User user, Joke joke, Category newCategory, String content) {
        Category savedCategory;
        if(newCategory != null){
             savedCategory = categoryRepository.findById(newCategory.getId()).orElse(null);

            if(savedCategory == null){
                return ErrorResponse.build(3, "There is no category with given ID");
            }
        }
        else{
            savedCategory = null;
        }

        EditedJoke editedJoke = new EditedJoke();
        editedJoke.setJoke(joke);
        editedJoke.setUser(user);
        editedJoke.setCategory(savedCategory);
        editedJoke.setContent(content);
        editedJokeRepository.save(editedJoke);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }
    private ResponseEntity<Response> editByStaff(Joke joke, Category newCategory, String content) {
        if(newCategory != null){
            Category savedCategory = categoryRepository.findById(newCategory.getId()).orElse(null);

            if(savedCategory == null){
                return ErrorResponse.build(3, "There is no category with given ID");
            }

            //Category of the joke has been changed
            if(!savedCategory.equals(joke.getCategory())){
                joke.getCategory().changeJokeAmount(-1);
                joke.setCategory(savedCategory);

                savedCategory.changeJokeAmount(1);
            }
        }
        else{
            Category jokeCategory = joke.getCategory();
            if(jokeCategory != null){
                jokeCategory.changeJokeAmount(-1);
                joke.setCategory(null);
            }
        }

        joke.setContent(content);
        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }
    private ResponseEntity<Response> checkRequirements(Joke joke, User user, String content) {
        if(joke == null){
            return ErrorResponse.build(3, "There is no joke with given id");
        }

        if(user == null){
            return ErrorResponse.build(4, "Your session has been expired");
        }

        ResponseEntity<Response> contentVerificationResponse = jokeService.checkJokeContent(content);
        if(contentVerificationResponse.getStatusCode() != HttpStatus.OK){
            return contentVerificationResponse;
        }

        return ResponseEntity.ok().build();
    }
}

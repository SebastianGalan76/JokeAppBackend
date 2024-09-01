package com.coresaken.JokeApp.service.editedjoke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.EditedJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
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
public class EditedJokeService {
    final UserService userService;

    final EditedJokeRepository editedJokeRepository;
    final JokeRepository jokeRepository;

    public void create(Joke joke, User user, Category category, String content, Joke.Type type, Joke.Kind kind){
        EditedJoke editedJoke = editedJokeRepository.findByJoke(joke);
        if(editedJoke == null){
            editedJoke = new EditedJoke();
        }
        editedJoke.setType(type);
        editedJoke.setKind(kind);

        editedJoke.setJoke(joke);
        editedJoke.setUser(user);
        editedJoke.setCategory(category);
        editedJoke.setContent(content);
        editedJokeRepository.save(editedJoke);
    }

    @Transactional
    public ResponseEntity<Response> accept(Long id) {
        EditedJoke editedJoke = editedJokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> verificationRequirementsResponse = checkRequirements(editedJoke, user);
        if(verificationRequirementsResponse.getStatusCode() != HttpStatus.OK){
            return verificationRequirementsResponse;
        }

        assert editedJoke != null;
        Joke joke = editedJoke.getJoke();
        joke.setContent(editedJoke.getContent());

        Category currentCategory = joke.getCategory();
        Category newCategory = editedJoke.getCategory();

        if(currentCategory!=null && !currentCategory.equals(newCategory)){
            currentCategory.changeJokeAmount(-1);
        }

        if(newCategory!=null){
            newCategory.changeJokeAmount(1);
        }

        joke.setCategory(newCategory);
        editedJokeRepository.delete(editedJoke);
        jokeRepository.save(joke);
        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Response> reject(Long id) {
        EditedJoke editedJoke = editedJokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> verificationRequirementsResponse = checkRequirements(editedJoke, user);
        if(verificationRequirementsResponse.getStatusCode() != HttpStatus.OK){
            return verificationRequirementsResponse;
        }

        assert editedJoke != null;
        editedJokeRepository.delete(editedJoke);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    private ResponseEntity<Response> checkRequirements(EditedJoke editedJoke, User user) {
        if(editedJoke == null){
            return ErrorResponse.build(1, "There is no edited joke with given id");
        }

        if(user == null){
            return ErrorResponse.build(2, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }

        if(!PermissionChecker.hasPermission(user, User.Role.HELPER)){
            return ErrorResponse.build(3, "You don't have required permission");
        }

        return ResponseEntity.ok().build();
    }
}

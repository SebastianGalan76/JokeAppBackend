package com.coresaken.JokeApp.service.editedjoke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EditedJokeService {
    final UserService userService;

    final EditedJokeRepository editedJokeRepository;
    final JokeRepository jokeRepository;
    final CategoryRepository categoryRepository;

    public void edit(Joke joke, User user, List<Category> categories, String content, Joke.Type type, Joke.Kind kind){
        EditedJoke editedJoke = editedJokeRepository.findByJoke(joke);
        if(editedJoke == null){
            editedJoke = new EditedJoke();
        }
        editedJoke.setType(type);
        editedJoke.setKind(kind);

        editedJoke.setJoke(joke);
        editedJoke.setUser(user);

        editedJoke.setCategories(categories);

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

        updateCategoryJokeAmount(joke.getCategories(), editedJoke.getCategories());
        List<Category> categories = editedJoke.getCategories();
        List<Category> savedCategories = new ArrayList<>();

        if(categories != null){
            for(Category category:categories){
                Category savedCategory = categoryRepository.findById(category.getId()).orElse(null);

                if(savedCategory != null){
                    savedCategories.add(savedCategory);
                }
            }
        }

        joke.setCategories(savedCategories);
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

    public void updateCategoryJokeAmount(List<Category> oldList, List<Category> newList) {
        List<Category> toRemove = oldList.stream()
                .filter(category -> !newList.contains(category))
                .toList();

        List<Category> toAdd = newList.stream()
                .filter(category -> !oldList.contains(category))
                .toList();

        toRemove.forEach(category -> category.changeJokeAmount(-1));
        toAdd.forEach(category -> category.changeJokeAmount(1));
    }
}

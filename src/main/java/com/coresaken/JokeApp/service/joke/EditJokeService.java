package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.EditedJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.editedjoke.EditedJokeService;
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
public class EditJokeService {
    final UserService userService;
    final JokeService jokeService;
    final EditedJokeService editedJokeService;

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
        Joke.Type type = Joke.Type.valueOf((jokeDto.getType() == null || jokeDto.getType().isBlank()) ? "JOKE" : jokeDto.getType());
        Joke.Kind kind = Joke.Kind.valueOf((jokeDto.getKind() == null || jokeDto.getKind().isBlank()) ? "TRADITIONAL" : jokeDto.getKind());

        assert joke != null;

        if(PermissionChecker.hasPermission(user, User.Role.HELPER)){
            return editByStaff(joke, jokeDto.getCategories(), content, type, kind);
        }
        else{
            return editByUser(user, joke, jokeDto.getCategories(), content, type, kind);
        }
    }

    private ResponseEntity<Response> editByUser(User user, Joke joke, List<Category> categories, String content, Joke.Type type, Joke.Kind kind) {
        List<Category> savedCategories = new ArrayList<>();
        if(categories != null){
             for(Category category:categories){
                 Category savedCategory = categoryRepository.findById(category.getId()).orElse(null);

                 if(savedCategory != null){
                     savedCategories.add(savedCategory);
                 }
             }
        }

        editedJokeService.edit(joke, user, savedCategories, content, type, kind);

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }
    private ResponseEntity<Response> editByStaff(Joke joke, List<Category> categories, String content, Joke.Type type, Joke.Kind kind) {
        updateCategoryJokeAmount(joke.getCategories(), categories);

        joke.setType(type);
        joke.setKind(kind);

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

    public void updateCategoryJokeAmount(List<Category> oldList, List<Category> newList) {
        if(newList == null || newList.isEmpty()){
            oldList.forEach(category -> category.changeJokeAmount(-1));
        }
        else{

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
}

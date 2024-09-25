package com.coresaken.JokeApp;

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
import com.coresaken.JokeApp.service.editedjoke.EditedJokeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class EditedJokeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private EditedJokeRepository editedJokeRepository;
    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private EditedJokeService editedJokeService;

    public EditedJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void accept_UserDoesNotHavePermission(){
        User user = new User();
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        Long editedJokeId = 1L;
        EditedJoke editedJoke = new EditedJoke();
        when(editedJokeRepository.findById(editedJokeId)).thenReturn(Optional.of(editedJoke));

        ResponseEntity<Response> responseEntity = editedJokeService.accept(editedJokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
        assertEquals(3, response.getError().getCode());
    }

    @Test
    public void accept_Success(){
        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        Joke joke = new Joke();
        joke.setContent("Current Joke Content");

        Category currentCategory = new Category();
        currentCategory.setId(1L);
        currentCategory.setJokeAmount(100);
        joke.setCategories(new ArrayList<>(List.of(currentCategory)));

        Long editedJokeId = 1L;
        String editedJokeContent = "Edited Joke Content";
        EditedJoke editedJoke = new EditedJoke();
        editedJoke.setId(editedJokeId);
        editedJoke.setContent(editedJokeContent);
        editedJoke.setJoke(joke);

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setJokeAmount(100);
        editedJoke.setCategories(new ArrayList<>(List.of(newCategory)));

        when(editedJokeRepository.findById(editedJokeId)).thenReturn(Optional.of(editedJoke));

        ResponseEntity<Response> responseEntity = editedJokeService.accept(editedJokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        assertNull(response.getError());

        assertEquals(editedJokeContent, joke.getContent());
        assertEquals(101, newCategory.getJokeAmount());
        assertEquals(99, currentCategory.getJokeAmount());
    }

    @Test
    public void reject_Success(){
        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        String currentJokeContent = "Current Joke Content";
        Joke joke = new Joke();
        joke.setContent(currentJokeContent);

        Category currentCategory = new Category();
        currentCategory.setId(1L);
        currentCategory.setJokeAmount(100);
        joke.setCategories(List.of(currentCategory));

        Long editedJokeId = 1L;
        String editedJokeContent = "Edited Joke Content";
        EditedJoke editedJoke = new EditedJoke();
        editedJoke.setId(editedJokeId);
        editedJoke.setContent(editedJokeContent);
        editedJoke.setJoke(joke);

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setJokeAmount(100);
        editedJoke.setCategories(List.of(currentCategory));

        when(editedJokeRepository.findById(editedJokeId)).thenReturn(Optional.of(editedJoke));

        ResponseEntity<Response> responseEntity = editedJokeService.reject(editedJokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        assertNull(response.getError());

        assertEquals(currentJokeContent, joke.getContent());
        assertEquals(100, newCategory.getJokeAmount());
        assertEquals(100, currentCategory.getJokeAmount());
    }
}

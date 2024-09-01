package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.DeleteJokeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class DeleteJokeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private DeleteJokeService deleteJokeService;

    public DeleteJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void delete_JokeHasCategory(){
        Category category = new Category();
        category.setJokeAmount(100);

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setCategory(category);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = deleteJokeService.delete(jokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(joke.getCategory());
        assertEquals(99, category.getJokeAmount());
        verify(jokeRepository).delete(joke);
    }

    @Test
    public void delete_UserDoesNotHavePermission(){
        Category category = new Category();
        category.setJokeAmount(100);

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setCategory(category);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = deleteJokeService.delete(jokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertNotNull(joke.getCategory());
        assertEquals(100, category.getJokeAmount());
        verify(jokeRepository, never()).delete(joke);
    }
}

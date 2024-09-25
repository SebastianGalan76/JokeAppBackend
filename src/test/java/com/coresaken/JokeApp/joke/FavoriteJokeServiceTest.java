package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.FavoriteJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.FavoriteJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.FavoriteJokeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FavoriteJokeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JokeRepository jokeRepository;
    @Mock
    private FavoriteJokeRepository favoriteJokeRepository;

    @InjectMocks
    private FavoriteJokeService favoriteJokeService;

    public FavoriteJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void toggle_UserNotLoggedIn(){
        when(userService.getLoggedUser()).thenReturn(null);

        ResponseEntity<Response> responseEntity = favoriteJokeService.toggle(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(1, response.getError().getCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
    }

    @Test
    public void toggle_NoJokeWithGivenId(){
        when(userService.getLoggedUser()).thenReturn(new User());
        when(jokeRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = favoriteJokeService.toggle(-1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(2, response.getError().getCode());
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
    }

    @Test
    public void toggle_AddToFavorite(){
        User user = new User();
        Joke joke = new Joke();

        when(userService.getLoggedUser()).thenReturn(user);
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(joke));

        ResponseEntity<Response> responseEntity = favoriteJokeService.toggle(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());

        verify(favoriteJokeRepository).save(any(FavoriteJoke.class));
    }

    @Test
    public void toggle_RemoveToFavorite(){
        User user = new User();
        Joke joke = new Joke();
        FavoriteJoke favoriteJoke = new FavoriteJoke();

        when(userService.getLoggedUser()).thenReturn(user);
        when(jokeRepository.findById(1L)).thenReturn(Optional.of(joke));
        when(favoriteJokeRepository.findByUserAndJoke(user, joke)).thenReturn(Optional.of(favoriteJoke));

        ResponseEntity<Response> responseEntity = favoriteJokeService.toggle(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
    }
}

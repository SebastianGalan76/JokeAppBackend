package com.coresaken.JokeApp.jokelist;

import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.jokelist.JokeListContainerService;
import com.coresaken.JokeApp.service.jokelist.JokeListService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class JokeListServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JokeListRepository jokeListRepository;
    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private JokeListContainerService jokeListContainerService;

    public JokeListServiceTest(){
        openMocks(this);
    }

    @Test
    public void addJokeToList_Success(){
        User user = new User();

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);

        Long jokeListId = 1L;
        JokeList jokeList = new JokeList();
        jokeList.setId(jokeListId);
        jokeList.setUser(user);

        when(userService.getLoggedUser()).thenReturn(user);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));
        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.of(jokeList));

        ResponseEntity<Response> responseEntity = jokeListContainerService.addJokeToList(jokeListId, jokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(1, jokeList.getJokes().size());
    }

    @Test
    public void deleteJokeFromList_Success(){
        User user = new User();

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);

        Long jokeListId = 1L;
        JokeList jokeList = new JokeList();
        jokeList.setId(jokeListId);
        jokeList.setUser(user);
        jokeList.getJokes().add(joke);

        when(userService.getLoggedUser()).thenReturn(user);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));
        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.of(jokeList));

        ResponseEntity<Response> responseEntity = jokeListContainerService.deleteJokeFromList(jokeListId, jokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(0, jokeList.getJokes().size());
    }
}

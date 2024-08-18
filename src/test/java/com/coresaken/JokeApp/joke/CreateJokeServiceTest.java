package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.CreateJokeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateJokeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private CreateJokeService createJokeService;

    public CreateJokeServiceTest() {
        openMocks(this);
    }

    @Test
    public void create_contentTooShort(){
        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent("SPAM");

        ResponseEntity<Response> responseEntity = createJokeService.create(jokeDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    public void create_UserWithHelperRole(){
        User user = new User();
        user.setRole(User.Role.HELPER);

        when(userService.getLoggedUser()).thenReturn(user);

        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent("What do you call fake spaghetti? \n" +
                "An impasta!");

        ResponseEntity<Response> responseEntity = createJokeService.create(jokeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());

        ArgumentCaptor<Joke> jokeCaptor = ArgumentCaptor.forClass(Joke.class);
        verify(jokeRepository).save(jokeCaptor.capture());

        Joke savedJoke = jokeCaptor.getValue();
        assertEquals("What do you call fake spaghetti? \n" +
                "An impasta!", savedJoke.getContent());
        assertEquals(0, savedJoke.getLikeAmount());
        assertEquals(0, savedJoke.getDislikeAmount());
        assertEquals(Joke.StatusType.ACCEPTED, savedJoke.getStatus());
        assertNull(savedJoke.getCategory());
    }
    @Test
    public void create_UserWithUserRole(){
        User user = new User();
        user.setRole(User.Role.USER);

        when(userService.getLoggedUser()).thenReturn(user);

        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent("What do you call fake spaghetti? \n" +
                "An impasta!");

        ResponseEntity<Response> responseEntity = createJokeService.create(jokeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());

        ArgumentCaptor<Joke> jokeCaptor = ArgumentCaptor.forClass(Joke.class);
        verify(jokeRepository).save(jokeCaptor.capture());

        Joke savedJoke = jokeCaptor.getValue();
        assertEquals("What do you call fake spaghetti? \n" +
                "An impasta!", savedJoke.getContent());
        assertEquals(0, savedJoke.getLikeAmount());
        assertEquals(0, savedJoke.getDislikeAmount());
        assertEquals(Joke.StatusType.NOT_VERIFIED, savedJoke.getStatus());
        assertNull(savedJoke.getCategory());
    }
}

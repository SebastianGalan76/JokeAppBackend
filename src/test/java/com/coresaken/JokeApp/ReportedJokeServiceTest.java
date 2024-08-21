package com.coresaken.JokeApp;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.ReportedJokeRepository;
import com.coresaken.JokeApp.service.ReportedJokeService;
import com.coresaken.JokeApp.service.UserService;
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

public class ReportedJokeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private ReportedJokeRepository reportedJokeRepository;
    @Mock
    private JokeRepository jokeRepository;
    @InjectMocks
    private ReportedJokeService reportedJokeService;

    public ReportedJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void accept_UserNotLoggedIn(){
        Long reportedJokeId = 1L;
        ReportedJoke reportedJoke = new ReportedJoke();
        reportedJoke.setId(reportedJokeId);

        when(reportedJokeRepository.findById(reportedJokeId)).thenReturn(Optional.of(reportedJoke));

        ResponseEntity<Response> responseEntity = reportedJokeService.accept(reportedJokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
        assertEquals(2, response.getError().getCode());
    }

    @Test
    public void accept_UserDoesNotHavePermission(){
        Long reportedJokeId = 1L;
        ReportedJoke reportedJoke = new ReportedJoke();
        reportedJoke.setId(reportedJokeId);

        when(reportedJokeRepository.findById(reportedJokeId)).thenReturn(Optional.of(reportedJoke));

        User user = new User();
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = reportedJokeService.accept(reportedJokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
        assertEquals(3, response.getError().getCode());
    }

    @Test
    public void accept_Success(){
        Long reportedJokeId = 1L;

        Joke joke = new Joke();

        ReportedJoke reportedJoke = new ReportedJoke();
        reportedJoke.setId(reportedJokeId);
        reportedJoke.setJoke(joke);

        when(reportedJokeRepository.findById(reportedJokeId)).thenReturn(Optional.of(reportedJoke));

        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = reportedJokeService.accept(reportedJokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());
        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());

        verify(jokeRepository).delete(joke);
    }
}

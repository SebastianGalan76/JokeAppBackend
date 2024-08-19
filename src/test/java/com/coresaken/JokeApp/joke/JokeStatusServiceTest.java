package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.RejectionReason;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.RejectionReasonRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.CreateJokeService;
import com.coresaken.JokeApp.service.joke.JokeStatusService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class JokeStatusServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private RejectionReasonRepository rejectionReasonRepository;

    @InjectMocks
    private JokeStatusService jokeStatusService;

    public JokeStatusServiceTest() {
        openMocks(this);
    }

    @Test
    public void accept_IncorrectJokeId(){
        Long jokeId = -1L;
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = jokeStatusService.accept(jokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.status);
        assertEquals(1, response.error.getCode());
    }

    @Test
    public void accept_IncorrectUserPermission(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = jokeStatusService.accept(jokeId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.status);
        assertEquals(3, response.error.getCode());
    }

    @Test
    public void accept_Success(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = jokeStatusService.accept(jokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.status);
        assertNull(response.error);
        assertEquals(joke.getStatus(), Joke.StatusType.ACCEPTED);
    }

    @Test
    public void reject_IncorrectJokeId(){
        Long jokeId = -1L;
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = jokeStatusService.reject(jokeId, null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.status);
        assertEquals(1, response.error.getCode());
    }

    @Test
    public void reject_ReasonTooLong(){
        String reason = "A".repeat(1000);

        Long jokeId = 1L;

        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setStatus(Joke.StatusType.NOT_VERIFIED);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.HELPER);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = jokeStatusService.reject(jokeId, reason);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.status);
        assertEquals(4, response.error.getCode());
        assertEquals(Joke.StatusType.NOT_VERIFIED, joke.getStatus());
    }

    @Test
    public void reject_Success(){
        String reason = "Not funny";

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(1L);
        joke.setStatus(Joke.StatusType.NOT_VERIFIED);
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        user.setRole(User.Role.ADMIN);
        when(userService.getLoggedUser()).thenReturn(user);

        ResponseEntity<Response> responseEntity = jokeStatusService.reject(jokeId, reason);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.SUCCESS, response.status);
        assertNull(response.error);
        assertEquals(Joke.StatusType.REJECTED, joke.getStatus());
        assertNotNull(joke.getRejectionReason());
        assertEquals(reason, joke.getRejectionReason().getReason());
    }

    @Test
    public void sendToVerification_Success(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(1L);
        joke.setStatus(Joke.StatusType.REJECTED);
        joke.setRejectionReason(new RejectionReason());

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        ResponseEntity<Response> responseEntity = jokeStatusService.sendToVerification(jokeId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNull(joke.getRejectionReason());
        assertEquals(Joke.StatusType.NOT_VERIFIED, joke.getStatus());
    }
}

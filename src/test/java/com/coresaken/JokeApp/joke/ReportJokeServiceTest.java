package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.ReportedJoke;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.ReportedJokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.ReportJokeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class ReportJokeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JokeRepository jokeRepository;
    @Mock
    private ReportedJokeRepository reportedJokeRepository;

    @InjectMocks
    private ReportJokeService reportJokeService;

    public ReportJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void report_InvalidJokeId(){
        Long jokeId = -1L;
        when(jokeRepository.findById(jokeId)).thenReturn(Optional.empty());

        String reason = "A".repeat(20);
        ResponseEntity<Response> responseEntity = reportJokeService.report(jokeId, reason);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());

        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
        assertEquals(2, response.getError().getCode());
    }

    @Test
    public void report_ReasonTooLong_Substring(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        String reason = "A".repeat(550);
        ResponseEntity<Response> responseEntity = reportJokeService.report(jokeId, reason);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());

        ArgumentCaptor<ReportedJoke> reportedJokeArgumentCaptor = ArgumentCaptor.forClass(ReportedJoke.class);
        verify(reportedJokeRepository).save(reportedJokeArgumentCaptor.capture());

        ReportedJoke savedReportedJoke = reportedJokeArgumentCaptor.getValue();
        assertEquals(512, savedReportedJoke.getReason().length());
    }
}

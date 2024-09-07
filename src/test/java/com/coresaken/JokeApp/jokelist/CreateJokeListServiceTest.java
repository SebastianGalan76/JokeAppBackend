package com.coresaken.JokeApp.jokelist;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.jokelist.CreateJokeListService;
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

public class CreateJokeListServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeListRepository jokeListRepository;

    @InjectMocks
    private CreateJokeListService createJokeListService;

    public CreateJokeListServiceTest(){
        openMocks(this);
    }

    @Test
    public void create_UserNotLoggedIn(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("Black Humor");

        ResponseEntity<ResponseContent<com.coresaken.JokeApp.data.response.JokeListDto>> responseEntity = createJokeListService.create(jokeListDto, null);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void create_NameIsEmpty(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("      ");

        ResponseEntity<ResponseContent<com.coresaken.JokeApp.data.response.JokeListDto>> responseEntity = createJokeListService.create(jokeListDto, null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());

        assertEquals(1, response.getError().getCode());
    }

   /* @Test
    public void create_JokeListDtoWithoutVisibility(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("Black Humor");

        when(userService.getLoggedUser()).thenReturn(new User());

        ResponseEntity<ResponseContent<com.coresaken.JokeApp.data.response.JokeListDto>> responseEntity = createJokeListService.create(jokeListDto, null);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());

        ArgumentCaptor<JokeList> jokeListCaptor = ArgumentCaptor.forClass(JokeList.class);
        verify(jokeListRepository).save(jokeListCaptor.capture());

        JokeList capturedJokeList = jokeListCaptor.getValue();
        assertEquals(JokeList.VisibilityType.PRIVATE, capturedJokeList.getVisibilityType());

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
    }

    @Test
    public void create_CreatePublicJokeList(){
        JokeListDto jokeListDto = new JokeListDto();
        String jokeListName = "Black Humor";
        jokeListDto.setName(jokeListName);
        jokeListDto.setVisibilityType(JokeList.VisibilityType.PUBLIC);

        when(userService.getLoggedUser()).thenReturn(new User());

        ResponseEntity<ResponseContent<com.coresaken.JokeApp.data.response.JokeListDto>> responseEntity = createJokeListService.create(jokeListDto, null);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNull(response.getError());

        ArgumentCaptor<JokeList> jokeListCaptor = ArgumentCaptor.forClass(JokeList.class);
        verify(jokeListRepository).save(jokeListCaptor.capture());

        JokeList capturedJokeList = jokeListCaptor.getValue();
        assertEquals(JokeList.VisibilityType.PUBLIC, capturedJokeList.getVisibilityType());
        assertEquals(jokeListName, capturedJokeList.getName());

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
    }*/
}

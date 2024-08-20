package com.coresaken.JokeApp.jokelist;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.jokelist.EditJokeListService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

public class EditJokeListServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeListRepository jokeListRepository;

    @InjectMocks
    private EditJokeListService editJokeListService;

    public EditJokeListServiceTest(){
        openMocks(this);
    }

    @Test
    public void edit_UserNotLoggedIn(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("Black Humor");

        Long jokeListId = 1L;
        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.of(new JokeList()));

        ResponseEntity<Response> responseEntity = editJokeListService.edit(jokeListId, jokeListDto);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void edit_IncorrectJokeListId(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("Black Humor");

        Long jokeListId = -1L;

        User user = new User();
        when(userService.getLoggedUser()).thenReturn(user);

        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = editJokeListService.edit(jokeListId, jokeListDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());

        assertEquals(3, response.getError().getCode());
    }

    @Test
    public void edit_EditSomeoneJokeListAsUser(){
        JokeListDto jokeListDto = new JokeListDto();
        jokeListDto.setName("Black Humor");

        Long jokeListId = -1L;

        User user = new User();
        user.setId(1L);
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        JokeList jokeList = new JokeList();
        jokeList.setName("123qwe");
        jokeList.setUser(new User());

        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.of(jokeList));

        ResponseEntity<Response> responseEntity = editJokeListService.edit(jokeListId, jokeListDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());

        assertEquals(4, response.getError().getCode());
    }

    @Test
    public void edit_EditSomeoneJokeListAsModerator(){
        JokeListDto jokeListDto = new JokeListDto();
        String newName = "Black Humor";

        jokeListDto.setName(newName);

        Long jokeListId = 1L;

        User user = new User();
        user.setId(1L);
        user.setRole(User.Role.MODERATOR);
        when(userService.getLoggedUser()).thenReturn(user);

        JokeList jokeList = new JokeList();
        jokeList.setName("123qwe");
        jokeList.setUser(new User());

        when(jokeListRepository.findById(jokeListId)).thenReturn(Optional.of(jokeList));

        ResponseEntity<Response> responseEntity = editJokeListService.edit(jokeListId, jokeListDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        ArgumentCaptor<JokeList> jokeListCaptor = ArgumentCaptor.forClass(JokeList.class);
        verify(jokeListRepository).save(jokeListCaptor.capture());

        JokeList capturedJokeList = jokeListCaptor.getValue();
        assertEquals(newName, capturedJokeList.getName());
    }
}

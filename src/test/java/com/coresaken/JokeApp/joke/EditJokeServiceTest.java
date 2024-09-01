package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.dto.JokeDto;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.EditedJokeRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.editedjoke.EditedJokeService;
import com.coresaken.JokeApp.service.joke.EditJokeService;
import com.coresaken.JokeApp.service.joke.JokeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class EditJokeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JokeService jokeService;

    @Mock
    private JokeRepository jokeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EditedJokeRepository editedJokeRepository;
    @Mock
    private EditedJokeService editedJokeService;
    @InjectMocks
    private EditJokeService editJokeService;

    public EditJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void editJoke_asStaff_removeCategory(){
        User user = new User();
        user.setRole(User.Role.ADMIN);
        when(userService.getLoggedUser()).thenReturn(user);

        String newContent = "Funny Joke";

        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent(newContent);
        jokeDto.setCategory(null);

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);

        Category category = new Category();
        category.setJokeAmount(100);
        joke.setCategory(category);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        when(jokeService.checkJokeContent(jokeDto.getContent())).thenReturn(
                ResponseEntity.ok().build()
        );

        ResponseEntity<Response> responseEntity = editJokeService.editJoke(jokeId, jokeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(99, category.getJokeAmount());
        assertEquals(newContent, joke.getContent());
    }

    @Test
    public void editJoke_asStaff_changeCategory(){
        User user = new User();
        user.setRole(User.Role.ADMIN);
        when(userService.getLoggedUser()).thenReturn(user);

        String newContent = "Funny Joke";

        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent(newContent);

        Long newCategoryId = 1L;
        Category newCategory = new Category();
        newCategory.setId(newCategoryId);
        newCategory.setJokeAmount(100);
        jokeDto.setCategory(newCategory);

        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);

        Category category = new Category();
        category.setJokeAmount(100);
        joke.setCategory(category);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        when(jokeService.checkJokeContent(jokeDto.getContent())).thenReturn(
                ResponseEntity.ok().build()
        );

        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.of(newCategory));

        ResponseEntity<Response> responseEntity = editJokeService.editJoke(jokeId, jokeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(99, category.getJokeAmount());
        assertEquals(101, joke.getCategory().getJokeAmount());
        assertEquals(newContent, joke.getContent());
    }

    @Test
    public void editJoke_asUser_removeCategory(){
        User user = new User();
        user.setRole(User.Role.USER);
        when(userService.getLoggedUser()).thenReturn(user);

        String newContent = "Funny Joke";
        JokeDto jokeDto = new JokeDto();
        jokeDto.setContent(newContent);
        jokeDto.setCategory(null);

        Long jokeId = 1L;
        String currentContent = "Current Funny Joke";
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setContent(currentContent);

        Category category = new Category();
        category.setJokeAmount(100);
        joke.setCategory(category);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        when(jokeService.checkJokeContent(jokeDto.getContent())).thenReturn(
                ResponseEntity.ok().build()
        );

        ResponseEntity<Response> responseEntity = editJokeService.editJoke(jokeId, jokeDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(100, category.getJokeAmount());
        assertEquals(currentContent, joke.getContent());
        assertEquals(category, joke.getCategory());

        verify(editedJokeService).create(joke, user, null, newContent, Joke.Type.JOKE, Joke.Kind.TRADITIONAL);
    }
}

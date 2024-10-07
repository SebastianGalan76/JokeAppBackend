package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.response.JokeDto;
import com.coresaken.JokeApp.data.response.PageResponse;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.CategoryRepository;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.JokeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class JokeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private JokeService jokeService;

    public JokeServiceTest(){
        openMocks(this);
    }

    @Test
    void getJokesByCategory_Success() {
        Long categoryId = 1L;
        int page = 0;

        Category category = new Category();
        category.setId(categoryId);

        Joke joke = new Joke();
        joke.setContent("Funny joke");

        Page<Joke> jokesPage = new PageImpl<>(List.of(joke));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(jokeRepository.findByCategoriesAndAccepted(category, PageRequest.of(page, 15))).thenReturn(jokesPage);
        when(userService.getLoggedUser()).thenReturn(null);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        ResponseEntity<PageResponse<JokeDto>> responseEntity = jokeService.getJokesByCategory(categoryId, page, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        PageResponse<JokeDto> response = responseEntity.getBody();
        assertNotNull(response);

        assertTrue(response.getContent().getContent().contains(JokeDto.build(joke)));
    }

    @Test
    void getJokesByCategory_InvalidCategoryId() {
        Long categoryId = -11L;
        int page = 0;

        Joke joke = new Joke();
        joke.setContent("Funny joke");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        ResponseEntity<PageResponse<JokeDto>> responseEntity = jokeService.getJokesByCategory(categoryId, page, null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        PageResponse<JokeDto> response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getError());
        assertEquals(1, response.getError().getCode());
    }
}

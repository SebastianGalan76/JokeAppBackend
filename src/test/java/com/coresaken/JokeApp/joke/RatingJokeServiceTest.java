package com.coresaken.JokeApp.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.Rating;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.RatingRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.service.joke.RateJokeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class RatingJokeServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JokeRepository jokeRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private RateJokeService rateJokeService;

    public RatingJokeServiceTest(){
        openMocks(this);
    }

    @Test
    public void like_IncorrectJokeId(){
        Long jokeId = -1L;

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = rateJokeService.like(jokeId, request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(ResponseStatusEnum.ERROR, response.getStatus());
        assertNotNull(response.getError());
        assertEquals(1,response.getError().getCode());
    }

    @Test
    public void like_DeleteOwnRating(){
        deleteOwnRatingByReactionType(Rating.ReactionType.LIKE);
    }

    @Test
    public void dislike_DeleteOwnRating(){
        deleteOwnRatingByReactionType(Rating.ReactionType.DISLIKE);
    }

    @Test
    public void like_ChangeCurrentRatingToDislike(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setLikeAmount(100);
        joke.setDislikeAmount(100);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        String userIp = "192.168.0.1";
        when(userService.getLoggedUser()).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(userIp);

        Rating rating = new Rating();
        rating.setReactionType(Rating.ReactionType.DISLIKE);
        when(ratingRepository.findByJokeAndUserOrUserIp(joke, null, userIp)).thenReturn(Optional.of(rating));

        ResponseEntity<Response> responseEntity = rateJokeService.like(jokeId, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        verify(ratingRepository, never()).delete(rating);

        assertEquals(101, joke.getLikeAmount());
        assertEquals(99, joke.getDislikeAmount());
    }

    @Test
    public void dislike_ChangeCurrentRatingToLike(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setLikeAmount(100);
        joke.setDislikeAmount(100);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        String userIp = "192.168.0.1";
        when(userService.getLoggedUser()).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(userIp);

        Rating rating = new Rating();
        rating.setReactionType(Rating.ReactionType.LIKE);
        when(ratingRepository.findByJokeAndUserOrUserIp(joke, null, userIp)).thenReturn(Optional.of(rating));

        ResponseEntity<Response> responseEntity = rateJokeService.dislike(jokeId, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        verify(ratingRepository, never()).delete(rating);

        assertEquals(99, joke.getLikeAmount());
        assertEquals(101, joke.getDislikeAmount());
    }

    @Test
    public void like_CreateNewLikeRating(){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setLikeAmount(100);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        String userIp = "192.168.0.1";
        when(userService.getLoggedUser()).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn(userIp);

        when(ratingRepository.findByJokeAndUserOrUserIp(joke, null, userIp)).thenReturn(Optional.empty());

        ResponseEntity<Response> responseEntity = rateJokeService.like(jokeId, request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        verify(ratingRepository, never()).delete(any(Rating.class));
        verify(ratingRepository).save(any(Rating.class));

        assertEquals(101, joke.getLikeAmount());
    }

    public void deleteOwnRatingByReactionType(Rating.ReactionType reactionType){
        Long jokeId = 1L;
        Joke joke = new Joke();
        joke.setId(jokeId);
        joke.setLikeAmount(100);
        joke.setDislikeAmount(100);

        when(jokeRepository.findById(jokeId)).thenReturn(Optional.of(joke));

        User user = new User();
        String userIp = "192.168.0.1";
        when(userService.getLoggedUser()).thenReturn(user);
        when(request.getRemoteAddr()).thenReturn(userIp);

        Rating rating = new Rating();
        rating.setReactionType(reactionType);
        when(ratingRepository.findByJokeAndUserOrUserIp(joke, user, userIp)).thenReturn(Optional.of(rating));

        ResponseEntity<Response> responseEntity;
        if(reactionType == Rating.ReactionType.LIKE){
            responseEntity = rateJokeService.like(jokeId, request);
        }
        else{
            responseEntity = rateJokeService.dislike(jokeId, request);
        }

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Response response = responseEntity.getBody();
        assertNotNull(response);

        assertEquals(ResponseStatusEnum.SUCCESS, response.getStatus());
        verify(ratingRepository).delete(rating);

        if(reactionType == Rating.ReactionType.LIKE){
            assertEquals(99, joke.getLikeAmount());
        }
        if(reactionType == Rating.ReactionType.DISLIKE){
            assertEquals(99, joke.getDislikeAmount());
        }
    }
}

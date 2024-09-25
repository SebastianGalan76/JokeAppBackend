package com.coresaken.JokeApp.service.joke;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.model.joke.Rating;
import com.coresaken.JokeApp.database.repository.joke.JokeRepository;
import com.coresaken.JokeApp.database.repository.joke.RatingRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RateJokeService {
    final UserService userService;

    final JokeRepository jokeRepository;
    final RatingRepository ratingRepository;

    @Transactional
    public ResponseEntity<Response> like(Long id, HttpServletRequest request) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> response = checkRequirements(joke);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }
        assert joke != null;

        synchronized (joke){
            Rating rating = ratingRepository.findByJokeAndUserOrUserIp(joke, user, request.getRemoteAddr()).orElse(null);
            if(rating != null){
                //Delete existing like
                if(rating.getReactionType() == Rating.ReactionType.LIKE){
                    List<Rating> ratings = joke.getRatings();
                    if(ratings!=null){
                        ratings.remove(rating);
                        ratingRepository.delete(rating);
                        joke.changeLikeAmount(-1);
                    }
                }
                //Change dislike to like
                else{
                    rating.setReactionType(Rating.ReactionType.LIKE);
                    rating.setRatingAt(LocalDateTime.now());
                    joke.changeLikeAmount(1);
                    joke.changeDislikeAmount(-1);
                }
            }
            else{
                rating = buildRating(Rating.ReactionType.LIKE, joke, user, request);
                joke.changeLikeAmount(1);
                ratingRepository.save(rating);
            }
        }

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Response> dislike(Long id, HttpServletRequest request) {
        Joke joke = jokeRepository.findById(id).orElse(null);
        User user = userService.getLoggedUser();

        ResponseEntity<Response> response = checkRequirements(joke);
        if(response.getStatusCode() != HttpStatus.OK){
            return response;
        }

        assert joke != null;
        synchronized (joke){
            Rating rating = ratingRepository.findByJokeAndUserOrUserIp(joke, user, request.getRemoteAddr()).orElse(null);
            if(rating != null){
                //Delete existing dislike
                if(rating.getReactionType() == Rating.ReactionType.DISLIKE){
                    List<Rating> ratings = joke.getRatings();
                    if(ratings!=null){
                        ratings.remove(rating);
                        ratingRepository.delete(rating);
                        ratingRepository.flush();

                        joke.changeDislikeAmount(-1);
                    }
                }
                //Change like to dislike
                else{
                    rating.setReactionType(Rating.ReactionType.DISLIKE);
                    rating.setRatingAt(LocalDateTime.now());
                    joke.changeDislikeAmount(1);
                    joke.changeLikeAmount(-1);
                }
            }
            else{
                rating = buildRating(Rating.ReactionType.DISLIKE, joke, user, request);
                joke.changeDislikeAmount(1);
                ratingRepository.save(rating);
            }
        }

        return new ResponseEntity<>(Response.builder().status(ResponseStatusEnum.SUCCESS).build(), HttpStatus.OK);
    }

    private ResponseEntity<Response> checkRequirements(Joke joke){
        if(joke == null){
            return ErrorResponse.build(1, "There is no joke with given id");
        }

        return new ResponseEntity<>(new Response(), HttpStatus.OK);
    }

    private Rating buildRating(Rating.ReactionType reactionType, Joke joke, User user, HttpServletRequest request){
        Rating rating = new Rating();
        rating.setJoke(joke);
        rating.setUser(user);
        rating.setReactionType(reactionType);
        rating.setRatingAt(LocalDateTime.now());
        rating.setUserIp(request.getRemoteAddr());

        return rating;
    }
}

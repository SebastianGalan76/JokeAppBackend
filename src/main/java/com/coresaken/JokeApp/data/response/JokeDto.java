package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@NoArgsConstructor
public class JokeDto {
    Long id;

    String content;
    int charCount;

    Joke.StatusType status;

    Category category;
    User owner;

    LocalDateTime createdAt;

    int userRating = 0;

    int likeAmount = 0;
    int dislikeAmount = 0;

    boolean isFavorite;
    List<JokeListDto> jokeLists;

    public static JokeDto build(User loggedInUser, Joke joke){
        JokeDto jokeDto = build(joke);
        if(loggedInUser == null){
            return jokeDto;
        }

        Optional<Rating> userRating = joke.getRatings().stream().filter(rating -> rating.getUser().equals(loggedInUser)).findFirst();
        userRating.ifPresent(rating -> jokeDto.setUserRating(rating.getReactionType() == Rating.ReactionType.LIKE ? 1 : -1));

        List<FavoriteJoke> favoriteJokes = loggedInUser.getFavoriteJokes();
        Optional<FavoriteJoke> favoriteJoke = favoriteJokes.stream().filter(fj -> Objects.equals(fj.getId(), joke.getId())).findFirst();
        if(favoriteJoke.isPresent()){
            jokeDto.setFavorite(true);
        }

        List<JokeList> jokeLists = joke.getJokeLists();
        List<JokeList> filteredJokeLists = jokeLists.stream().filter(jokeList -> jokeList.getUser().equals(loggedInUser)).toList();

        if(!filteredJokeLists.isEmpty()){
            jokeDto.setJokeLists(new ArrayList<>());

            jokeDto.setJokeLists(filteredJokeLists.stream().map(jokeList -> new JokeListDto(jokeList.getId(), jokeList.getName())).toList());
        }

        return jokeDto;
    }

    public static JokeDto build(Joke joke){
        JokeDto jokeDto = new JokeDto();
        jokeDto.setId(joke.getId());
        jokeDto.setContent(joke.getContent());
        jokeDto.setCharCount(joke.getCharCount());
        jokeDto.setStatus(joke.getStatus());
        jokeDto.setCategory(joke.getCategory());
        jokeDto.setOwner(joke.getUser());
        jokeDto.setCreatedAt(joke.getCreatedAt());

        jokeDto.setLikeAmount(joke.getLikeAmount());
        jokeDto.setDislikeAmount(joke.getDislikeAmount());
        return jokeDto;
    }

    @Data
    @AllArgsConstructor
    public static class JokeListDto{
        Long id;
        String name;
    }
}

package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import com.coresaken.JokeApp.database.model.joke.*;
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
    Joke.Type type;
    Joke.Kind kind;

    List<Category> categories;
    Owner owner;

    LocalDateTime createdAt;

    int userRating = 0;

    int likeAmount = 0;
    int dislikeAmount = 0;

    boolean isFavorite;
    List<JokeListDto> jokeLists;

    public static JokeDto build(User loggedInUser, Joke joke, String ip){
        JokeDto jokeDto = build(joke);

        Optional<Rating> userRating = joke.getRatings().stream().filter(rating -> (rating.getUser() != null && rating.getUser().equals(loggedInUser)) || (loggedInUser == null && rating.getUserIp().equals(ip))).findFirst();
        userRating.ifPresent(rating -> jokeDto.setUserRating(rating.getReactionType() == Rating.ReactionType.LIKE ? 1 : -1));

        if(loggedInUser == null){
            return jokeDto;
        }

        List<FavoriteJoke> favoriteJokes = loggedInUser.getFavoriteJokes();

        Optional<FavoriteJoke> favoriteJoke = favoriteJokes.stream().filter(fj -> Objects.equals(fj.getJoke().getId(), joke.getId())).findFirst();
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
        jokeDto.setCategories(joke.getCategories());
        jokeDto.setOwner(joke.getUser() != null ? new Owner(joke.getUser().getId(), joke.getUser().getLogin()) : null);
        jokeDto.setCreatedAt(joke.getCreatedAt());
        jokeDto.setType(joke.getType());
        jokeDto.setKind(joke.getKind());

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

    @Data
    @AllArgsConstructor
    public static class Owner{
        Long id;
        String login;
    }
}

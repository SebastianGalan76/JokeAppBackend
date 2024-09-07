package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Joke;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeListDto {
    Long id;

    String name;
    UUID uuid;

    JokeList.VisibilityType visibilityType;
    Set<JokeDto> jokes = new HashSet<>();

    public static JokeListDto build(User loggedInUser, JokeList jokeList, HttpServletRequest request){
        JokeListDto jokeListDto = new JokeListDto();

        jokeListDto.setId(jokeList.getId());
        jokeListDto.setName(jokeList.getName());
        jokeListDto.setUuid(jokeList.getUuid());
        jokeListDto.setVisibilityType(jokeList.getVisibilityType());

        jokeListDto.setJokes(jokeList.getJokes().stream().map(joke -> JokeDto.build(loggedInUser, joke, request.getRemoteAddr())).collect(Collectors.toSet()));
        return jokeListDto;
    }
}

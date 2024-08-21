package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.database.model.joke.Joke;
import lombok.*;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeResponse extends Response{
    public Page<Joke> content;
}

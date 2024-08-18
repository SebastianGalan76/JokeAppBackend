package com.coresaken.JokeApp.data.dto;

import com.coresaken.JokeApp.database.model.joke.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeDto {
    Category category;
    String content;
}

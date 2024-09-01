package com.coresaken.JokeApp.data.dto;

import com.coresaken.JokeApp.database.model.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeDto {
    Category category;
    String content;

    String type; //Joke or rusk
    String kind; //Traditional or enigmatic
}

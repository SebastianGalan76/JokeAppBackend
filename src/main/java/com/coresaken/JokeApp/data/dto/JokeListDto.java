package com.coresaken.JokeApp.data.dto;

import com.coresaken.JokeApp.database.model.JokeList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeListDto {
    String name;
    JokeList.VisibilityType visibilityType;
}

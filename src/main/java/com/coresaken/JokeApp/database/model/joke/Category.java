package com.coresaken.JokeApp.database.model.joke;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    int jokeAmount;

    public void changeJokeAmount(int value){
        jokeAmount+=value;
        if(jokeAmount<0){
            jokeAmount = 0;
        }
    }
}

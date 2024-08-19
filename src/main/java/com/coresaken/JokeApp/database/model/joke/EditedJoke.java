package com.coresaken.JokeApp.database.model.joke;

import com.coresaken.JokeApp.database.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditedJoke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "joke_id")
    Joke joke;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category newCategory;

    @Column(length = 5000)
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

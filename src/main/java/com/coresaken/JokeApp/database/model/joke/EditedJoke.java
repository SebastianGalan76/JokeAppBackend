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

    @OneToOne
    @JoinColumn(name = "joke_id")
    Joke joke;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(length = 5000)
    String content;

    @Enumerated(EnumType.STRING)
    Joke.Type type;

    @Enumerated(EnumType.STRING)
    Joke.Kind kind;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}

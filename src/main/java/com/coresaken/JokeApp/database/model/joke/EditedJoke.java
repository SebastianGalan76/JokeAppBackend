package com.coresaken.JokeApp.database.model.joke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "edited_joke_category",
            joinColumns = @JoinColumn(name = "edited_joke_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    List<Category> categories;

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

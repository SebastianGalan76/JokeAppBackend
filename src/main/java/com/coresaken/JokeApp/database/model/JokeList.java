package com.coresaken.JokeApp.database.model;

import com.coresaken.JokeApp.database.model.joke.Joke;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JokeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30)
    String name;

    @Column(nullable = false, unique = true)
    UUID uuid;

    @Enumerated(EnumType.STRING)
    VisibilityType visibilityType;

    @ManyToMany
    @JoinTable(
            name = "joke_list_jokes",
            joinColumns = @JoinColumn(name = "joke_list_id"),
            inverseJoinColumns = @JoinColumn(name = "joke_id")
    )
    List<Joke> jokes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public enum VisibilityType{
        PRIVATE, PUBLIC, NOT_PUBLIC
    }
}

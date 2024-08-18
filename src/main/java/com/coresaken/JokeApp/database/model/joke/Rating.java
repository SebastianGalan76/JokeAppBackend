package com.coresaken.JokeApp.database.model.joke;

import com.coresaken.JokeApp.database.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "joke_id")
    Joke joke;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    String userIp;

    @Enumerated(EnumType.STRING)
    ReactionType reactionType;

    LocalDateTime ratingAt;

    public enum ReactionType {
        LIKE, DISLIKE
    }
}

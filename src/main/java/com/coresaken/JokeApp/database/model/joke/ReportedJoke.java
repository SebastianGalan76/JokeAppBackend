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
public class ReportedJoke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "joke_id")
    Joke joke;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    LocalDateTime reportedAt;

    @Column(length = 512)
    String reason;

}

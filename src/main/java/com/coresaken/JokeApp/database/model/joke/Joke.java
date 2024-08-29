package com.coresaken.JokeApp.database.model.joke;

import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Joke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 5000)
    String content;

    int charCount;

    @Enumerated(EnumType.STRING)
    StatusType status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Type type;

    @Enumerated(EnumType.STRING)
    Kind kind;

    LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    RejectionReason rejectionReason;

    @OneToOne(cascade = CascadeType.ALL)
    EditedJoke editedJokes;

    @ManyToMany(fetch = FetchType.EAGER)
    List<JokeList> jokeLists;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<Rating> ratings;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<ReportedJoke> reportedJokes;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<FavoriteJoke> favorites;

    int likeAmount = 0;
    int dislikeAmount = 0;

    public void changeLikeAmount(int value){
        likeAmount += value;
        if(likeAmount < 0){
            likeAmount = 0;
        }
    }
    public void changeDislikeAmount(int value){
        dislikeAmount += value;
        if(dislikeAmount < 0){
            dislikeAmount = 0;
        }
    }

    public enum StatusType {
        NOT_VERIFIED, ACCEPTED, REJECTED
    }

    public enum Type{
        JOKE,
        RUSK // :)
    }

    public enum Kind{
        TRADITIONAL,
        ENIGMATIC // Question - Answer
    }
}

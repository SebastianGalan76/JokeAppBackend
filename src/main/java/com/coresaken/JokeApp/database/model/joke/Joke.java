package com.coresaken.JokeApp.database.model.joke;

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

    LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rejection_reason_id")
    RejectionReason rejectionReason;

    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Rating> ratings;

    int likeAmount;
    int dislikeAmount;

    public enum StatusType {
        NOT_VERIFIED, ACCEPTED, REJECTED
    }
}

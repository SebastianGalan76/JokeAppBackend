package com.coresaken.JokeApp.service.editedjoke;

import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.model.joke.Category;
import com.coresaken.JokeApp.database.model.joke.EditedJoke;
import com.coresaken.JokeApp.database.model.joke.Joke;
import com.coresaken.JokeApp.database.repository.joke.EditedJokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditedJokeService {
    final EditedJokeRepository editedJokeRepository;

    public void create(Joke joke, User user, Category category, String content){
        EditedJoke editedJoke = editedJokeRepository.findByJoke(joke);
        if(editedJoke == null){
            editedJoke = new EditedJoke();
        }

        editedJoke.setJoke(joke);
        editedJoke.setUser(user);
        editedJoke.setCategory(category);
        editedJoke.setContent(content);
        editedJokeRepository.save(editedJoke);
    }
}

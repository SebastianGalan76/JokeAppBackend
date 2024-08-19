package com.coresaken.JokeApp.service.joke;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.util.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JokeService {

    public ResponseEntity<Response> checkJokeContent(String content){
        int contentLength = content.length();
        if(contentLength<30){
            return ErrorResponse.build(1, "Joke's content is too short");
        }
        if(contentLength>5000){
            return ErrorResponse.build(2, "Joke's content is too long");
        }

        return ResponseEntity.ok().build();
    }
}

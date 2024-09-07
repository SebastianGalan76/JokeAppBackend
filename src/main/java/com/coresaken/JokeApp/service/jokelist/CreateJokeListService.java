package com.coresaken.JokeApp.service.jokelist;

import com.coresaken.JokeApp.data.response.JokeListDto;
import com.coresaken.JokeApp.data.response.ResponseContent;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateJokeListService {
    final UserService userService;

    final JokeListRepository jokeListRepository;

    public ResponseEntity<ResponseContent<JokeListDto>> create(com.coresaken.JokeApp.data.dto.JokeListDto jokeListDto, HttpServletRequest request) {
        String name = jokeListDto.getName().trim();
        JokeList.VisibilityType visibilityType = jokeListDto.getVisibilityType();
        if(visibilityType == null){
            visibilityType = JokeList.VisibilityType.PRIVATE;
        }

        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseContent.buildError(1, "Nazwa listy nie może być pusta"));
        }
        if (name.length()>30) {
            return ResponseEntity.badRequest().body(ResponseContent.buildError(4, "Nazwa listy jest zbyt długa"));
        }

        User user = userService.getLoggedUser();
        if (user == null) {
            return new ResponseEntity<>(ResponseContent.buildError(2, "Twoja sesja wygasła. Zaloguj się ponownie"), HttpStatus.UNAUTHORIZED);
        }

        JokeList jokeList = new JokeList();
        jokeList.setName(name);
        jokeList.setUser(user);
        jokeList.setVisibilityType(visibilityType);

        boolean isSaved = false;
        while (!isSaved) {
            try {
                jokeList.setUuid(UUID.randomUUID());
                jokeList = jokeListRepository.save(jokeList);
                isSaved = true;
            } catch (DataIntegrityViolationException e) {
                if (!isUuidConflictException(e)) {
                    return ResponseEntity.badRequest().body(ResponseContent.buildError(3, "Wystąpił nieoczekiwany błąd. Spróbuj ponownie później"));
                }
            }
        }
        return ResponseEntity.ok(ResponseContent.buildSuccess(JokeListDto.build(user, jokeList, request)));
    }

    private boolean isUuidConflictException(DataIntegrityViolationException e) {
        return e.getCause() instanceof ConstraintViolationException &&
                e.getMessage().contains("constraint [unique_uuid]");
    }
}

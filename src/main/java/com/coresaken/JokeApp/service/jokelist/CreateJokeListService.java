package com.coresaken.JokeApp.service.jokelist;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
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

    public ResponseEntity<Response> create(JokeListDto jokeListDto) {
        String name = jokeListDto.getName().trim();
        JokeList.VisibilityType visibilityType = jokeListDto.getVisibilityType();
        if(visibilityType == null){
            visibilityType = JokeList.VisibilityType.PRIVATE;
        }

        if (name.isEmpty()) {
            return ErrorResponse.build(1, "The name of the list cannot be empty");
        }
        if (name.length()>30) {
            return ErrorResponse.build(4, "The name of the list is too long");
        }

        User user = userService.getLoggedUser();
        if (user == null) {
            return ErrorResponse.build(2, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }

        JokeList jokeList = new JokeList();
        jokeList.setName(name);
        jokeList.setUser(user);
        jokeList.setVisibilityType(visibilityType);

        boolean isSaved = false;
        while (!isSaved) {
            try {
                jokeList.setUuid(UUID.randomUUID());
                jokeListRepository.save(jokeList);
                isSaved = true;
            } catch (DataIntegrityViolationException e) {
                if (!isUuidConflictException(e)) {
                    return ErrorResponse.build(3, "An error occurred while creating the joke list");
                }
            }
        }

        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    private boolean isUuidConflictException(DataIntegrityViolationException e) {
        return e.getCause() instanceof ConstraintViolationException &&
                e.getMessage().contains("constraint [unique_uuid]");
    }
}

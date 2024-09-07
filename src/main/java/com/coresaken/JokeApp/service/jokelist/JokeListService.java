package com.coresaken.JokeApp.service.jokelist;

import com.coresaken.JokeApp.data.response.JokeListDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JokeListService {
    final UserService userService;

    final JokeListRepository jokeListRepository;

    public ResponseEntity<Response> deleteByUuid(UUID uuid) {
        User user = userService.getLoggedUser();
        JokeList jokeList = jokeListRepository.findByUuid(uuid).orElse(null);

        if(user == null){
            return ErrorResponse.build(1, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }
        if(jokeList == null){
            return ErrorResponse.build(2, "There is no joke list with given id");
        }
        if(!jokeList.getUser().equals(user) && PermissionChecker.hasPermission(user, User.Role.MODERATOR)){
            return ErrorResponse.build(3, "You don't have required permission do delete this joke list");
        }

        jokeListRepository.delete(jokeList);
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }

    public ResponseEntity<JokeListDto> getByUuid(UUID uuid, HttpServletRequest request) {
        User user = userService.getLoggedUser();
        JokeList jokeList = jokeListRepository.findByUuid(uuid).orElse(null);

        if(jokeList == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(JokeListDto.build(user, jokeList, request));
    }


}

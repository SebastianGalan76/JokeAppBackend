package com.coresaken.JokeApp.service.jokelist;

import com.coresaken.JokeApp.data.dto.JokeListDto;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.Response;
import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.database.repository.JokeListRepository;
import com.coresaken.JokeApp.service.UserService;
import com.coresaken.JokeApp.util.ErrorResponse;
import com.coresaken.JokeApp.util.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditJokeListService {
    final UserService userService;

    final JokeListRepository jokeListRepository;

    public ResponseEntity<Response> edit(Long id, JokeListDto jokeListDto) {
        String name = jokeListDto.getName().trim();
        JokeList.VisibilityType visibilityType = jokeListDto.getVisibilityType();
        if(visibilityType == null){
            visibilityType = JokeList.VisibilityType.PRIVATE;
        }

        if (name.isEmpty()) {
            return ErrorResponse.build(1, "The name of the list cannot be empty");
        }
        if (name.length()>30) {
            return ErrorResponse.build(5, "The name of the list is too long");
        }

        User user = userService.getLoggedUser();
        if (user == null) {
            return ErrorResponse.build(2, "Your session has been expired", HttpStatus.UNAUTHORIZED);
        }

        JokeList jokeList = jokeListRepository.findById(id).orElse(null);
        if(jokeList == null){
            return ErrorResponse.build(3, "There is no joke list with given id");
        }

        if(!jokeList.getUser().equals(user) && !PermissionChecker.hasPermission(user, User.Role.MODERATOR)){
            return ErrorResponse.build(4, "You don't have permission to edit this joke list");
        }

        jokeList.setName(name);
        jokeList.setVisibilityType(visibilityType);
        jokeListRepository.save(jokeList);
        return ResponseEntity.ok(Response.builder().status(ResponseStatusEnum.SUCCESS).build());
    }
}

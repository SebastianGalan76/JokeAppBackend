package com.coresaken.JokeApp.controller;

import com.coresaken.JokeApp.data.response.UserDto;
import com.coresaken.JokeApp.database.model.User;
import com.coresaken.JokeApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("/user")
    public UserDto getUser(){
        User user = userService.getLoggedUser();
        return UserDto.build(user);
    }
}

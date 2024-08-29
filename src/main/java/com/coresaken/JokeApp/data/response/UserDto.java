package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.database.model.JokeList;
import com.coresaken.JokeApp.database.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;

    String login;
    String email;

    User.Role role;
    List<JokeListDto> jokeLists;

    public static UserDto build(User user){
        if(user==null){
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        userDto.setJokeLists(user.getJokeLists().stream().map(jokeList -> JokeListDto.build(user, jokeList)).toList());
        return userDto;
    }
}

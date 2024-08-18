package com.coresaken.JokeApp.util;

import com.coresaken.JokeApp.database.model.User;

public class PermissionChecker {

    public static boolean hasPermission(User user, User.Role minRole){
        if(user == null){
            return false;
        }

        return user.getRole().value >= minRole.value;
    }
}

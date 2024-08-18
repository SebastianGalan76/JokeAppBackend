package com.coresaken.JokeApp.auth.util;

import java.security.SecureRandom;

public class RandomToken {
    final static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String getToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(30);

        for (int i = 0; i < 30; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}

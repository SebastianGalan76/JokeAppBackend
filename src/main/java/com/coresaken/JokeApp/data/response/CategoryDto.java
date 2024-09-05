package com.coresaken.JokeApp.data.response;

public record CategoryDto(Long id, String name, String url, int jokeAmount, boolean isFavorite, int index) {

}

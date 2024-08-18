package com.coresaken.JokeApp.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorStatusResponse {
    int code;
    String message;
}

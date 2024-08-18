package com.coresaken.JokeApp.util;

import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.ErrorStatusResponse;
import com.coresaken.JokeApp.data.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    public static ResponseEntity<Response> build(int errorCode, String message){
        return build(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    public static ResponseEntity<Response> build(int errorCode, String message, HttpStatus status){
        ErrorStatusResponse error = new ErrorStatusResponse(errorCode, message);
        Response response = Response.builder()
                .status(ResponseStatusEnum.ERROR)
                .error(error)
                .build();
        return new ResponseEntity<>(response, status);
    }
}

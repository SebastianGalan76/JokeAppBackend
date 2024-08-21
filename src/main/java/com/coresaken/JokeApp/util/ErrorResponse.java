package com.coresaken.JokeApp.util;

import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.ErrorStatusResponse;
import com.coresaken.JokeApp.data.response.JokeResponse;
import com.coresaken.JokeApp.data.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    public static ResponseEntity<Response> build(int errorCode, String message){
        return build(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    public static ResponseEntity<Response> build(int errorCode, String message, HttpStatus status){
        return new ResponseEntity<>(buildErrorResponse(errorCode, message), status);
    }

    public static ResponseEntity<JokeResponse> buildJokeResponse(int errorCode, String message){
        return buildJokeResponse(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    public static ResponseEntity<JokeResponse> buildJokeResponse(int errorCode, String message, HttpStatus status){
        ErrorStatusResponse error = new ErrorStatusResponse(errorCode, message);
        JokeResponse response = new JokeResponse();
        response.setStatus(ResponseStatusEnum.ERROR);
        response.setError(error);

        return new ResponseEntity<>(response, status);
    }

    private static Response buildErrorResponse(int errorCode, String message){
        ErrorStatusResponse error = new ErrorStatusResponse(errorCode, message);
        return Response.builder()
                .status(ResponseStatusEnum.ERROR)
                .error(error)
                .build();
    }
}

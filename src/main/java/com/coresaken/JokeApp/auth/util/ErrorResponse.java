package com.coresaken.JokeApp.auth.util;

import com.coresaken.JokeApp.auth.dto.response.AuthenticationResponse;
import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.ErrorStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
    public static ResponseEntity<AuthenticationResponse> build(int errorCode, String message){
        ErrorStatusResponse error = new ErrorStatusResponse(errorCode, message);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .status(ResponseStatusEnum.ERROR)
                .error(error)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

package com.coresaken.JokeApp.util;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import com.coresaken.JokeApp.data.response.ErrorStatusResponse;
import com.coresaken.JokeApp.data.response.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorPageResponse<T> {
    public ResponseEntity<PageResponse<T>> build(int errorCode, String message){
        return build(errorCode, message, HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<PageResponse<T>> build(int errorCode, String message, HttpStatus status){
        ErrorStatusResponse error = new ErrorStatusResponse(errorCode, message);
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setStatus(ResponseStatusEnum.ERROR);
        pageResponse.setError(error);

        return new ResponseEntity<>(pageResponse, status);
    }
}

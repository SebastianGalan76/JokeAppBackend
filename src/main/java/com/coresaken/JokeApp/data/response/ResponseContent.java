package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContent<T> extends Response{
    T content;

    public static <T> ResponseContent<T> buildSuccess(T content){
        ResponseContent<T> responseContent = new ResponseContent<>();
        responseContent.setStatus(ResponseStatusEnum.SUCCESS);
        responseContent.setContent(content);

        return responseContent;
    }

    public static <T> ResponseContent<T> buildError(int errorCode, String message){
        ResponseContent<T> responseContent = new ResponseContent<>();
        responseContent.setStatus(ResponseStatusEnum.ERROR);
        responseContent.setError(new ErrorStatusResponse(errorCode, message));

        return responseContent;
    }
}

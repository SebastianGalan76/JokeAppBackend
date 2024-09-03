package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import lombok.*;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> extends Response{
    public Page<T> content;

    public static <T> PageResponse<T> buildSuccess(Page<T> content){
        PageResponse<T> response = new PageResponse<>();

        response.setStatus(ResponseStatusEnum.SUCCESS);
        response.setContent(content);
        return response;
    }

    public static <T> PageResponse<T> buildError(int errorCode, String message){
        PageResponse<T> response = new PageResponse<>();

        response.setStatus(ResponseStatusEnum.ERROR);
        response.setError(new ErrorStatusResponse(errorCode, message));
        return response;
    }
}

package com.coresaken.JokeApp.data.response;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> extends Response{
    public Page<T> content;
}

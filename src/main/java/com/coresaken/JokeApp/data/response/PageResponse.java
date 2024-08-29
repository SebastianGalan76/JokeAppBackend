package com.coresaken.JokeApp.data.response;

import lombok.*;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> extends Response{
    public Page<T> content;
}

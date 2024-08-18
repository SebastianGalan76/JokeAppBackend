package com.coresaken.JokeApp.data.response;

import com.coresaken.JokeApp.data.enums.ResponseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    public ResponseStatusEnum status;
    public ErrorStatusResponse error;
}

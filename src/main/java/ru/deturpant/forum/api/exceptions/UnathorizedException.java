package ru.deturpant.forum.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnathorizedException extends RuntimeException{
    public UnathorizedException(String msg) {
        super(msg);
    }

}

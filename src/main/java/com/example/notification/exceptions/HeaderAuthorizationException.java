package com.example.notification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class HeaderAuthorizationException extends RuntimeException {

    public HeaderAuthorizationException(String message){
        super(message);
    }

}

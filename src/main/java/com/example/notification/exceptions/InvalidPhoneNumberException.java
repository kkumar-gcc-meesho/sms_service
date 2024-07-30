package com.example.notification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "invalid phoneNumber";

    public InvalidPhoneNumberException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}

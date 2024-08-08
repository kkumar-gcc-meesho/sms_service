package com.example.notification.exceptions;

import com.example.notification.constants.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
        super(Message.ERROR_PHONE_NUMBER_INVALID);
    }

}

package com.example.notification.exceptions;

import com.example.notification.constants.Message;

public class BlacklistedPhoneNumberException extends RuntimeException {

    public BlacklistedPhoneNumberException() {
        super(Message.ERROR_PHONE_NUMBER_BLACKLISTED);
    }

}

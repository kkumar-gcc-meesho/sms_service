package com.example.notification.exceptions;

public class BlacklistedPhoneNumberException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "The phone number is blacklisted.";

    public BlacklistedPhoneNumberException() {
        super(DEFAULT_MESSAGE);
    }

}

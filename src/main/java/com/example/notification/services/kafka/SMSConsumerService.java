package com.example.notification.services.kafka;

import com.example.notification.exceptions.BlacklistedPhoneNumberException;

public interface SMSConsumerService {

    void listen(String smsId) throws BlacklistedPhoneNumberException;

}

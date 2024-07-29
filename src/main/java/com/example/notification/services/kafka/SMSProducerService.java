package com.example.notification.services.kafka;


import com.example.notification.dto.SMSDto;

public interface SMSProducerService {
    Long send(SMSDto smsDto);
}

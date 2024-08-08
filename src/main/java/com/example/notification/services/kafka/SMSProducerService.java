package com.example.notification.services.kafka;


import com.example.notification.dto.SMSDto;

import java.util.UUID;

public interface SMSProducerService {
    UUID send(SMSDto smsDto);
}

package com.example.notification.services.kafka;

import com.example.notification.constants.Kafka;
import com.example.notification.dto.SMSDto;
import com.example.notification.services.sms.SMSService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SMSProducerServiceImpl implements SMSProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SMSService smsService;

    @Override
    public UUID send(SMSDto smsDto) {
        SMSDto sms = smsService.createSMS(smsDto);
        kafkaTemplate.send(Kafka.SMS_TOPIC, String.valueOf(sms.getId()));

        return sms.getId();
    }
}

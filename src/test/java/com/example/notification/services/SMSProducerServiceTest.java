package com.example.notification.services;

import com.example.notification.constants.Kafka;
import com.example.notification.dto.SMSDto;
import com.example.notification.services.kafka.SMSProducerServiceImpl;
import com.example.notification.services.sms.SMSService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class SMSProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private SMSService smsService;

    @InjectMocks
    private SMSProducerServiceImpl smsProducerService;

    @Test
    public void send() {
        SMSDto smsDto = new SMSDto();
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Hello World");

        smsDto.setId(1L);

        when(smsService.createSMS(any(SMSDto.class))).thenReturn(smsDto);

        smsProducerService.send(smsDto);

        verify(kafkaTemplate, times(1)).send(Kafka.SMS_TOPIC, String.valueOf(smsDto.getId()));
    }
}

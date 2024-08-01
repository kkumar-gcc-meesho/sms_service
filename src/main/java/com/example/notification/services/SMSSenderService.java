package com.example.notification.services;

import com.example.notification.config.SMSConfig;
import com.example.notification.constants.Key;
import lombok.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Setter
@RequiredArgsConstructor
public class SMSSenderService {

    private final RestClient restClient = RestClient.create();

    private final SMSConfig smsConfig;

    public ResponseEntity<SMSSenderResponse> send(SMSSenderPayload smsSenderPayload) {

           return restClient.post()
                .uri(smsConfig.getMessagingApiUrl())
                .header(Key.SMS_SENDER_AUTH_KEY, smsConfig.getMessagingApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(smsSenderPayload.toJson()).retrieve().toEntity(SMSSenderResponse.class);

    }

}

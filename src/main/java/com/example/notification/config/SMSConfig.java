package com.example.notification.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SMSConfig {

    @Value("${sms.messaging.api.url}")
    private String messagingApiUrl;

    @Value("${sms.messaging.api.key}")
    private String messagingApiKey;

    @Value("${sms.blacklist.redis.key}")
    private String redisBlacklistKey;

}


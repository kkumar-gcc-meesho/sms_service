package com.example.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.username}")
    private String username;

    @Value("${redis.password}")
    private String password;

    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled(host, port, username, password);
    }

}



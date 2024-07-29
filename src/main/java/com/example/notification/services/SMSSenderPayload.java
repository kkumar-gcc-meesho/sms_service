package com.example.notification.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SMSSenderPayload {
    private String phoneNumber;
    private String message;
    private String correlationId;
}

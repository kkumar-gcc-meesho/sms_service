package com.example.notification.services;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSSenderResponse {
    private Response response;
}

@Data
@Getter
@Setter
class Response {
    private String code;
    private String description;
    private String transactionId;
}

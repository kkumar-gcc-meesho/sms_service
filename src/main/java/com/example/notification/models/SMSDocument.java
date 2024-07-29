package com.example.notification.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "sms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSDocument {

    private Long id;
    private String phoneNumber;
    private String message;
    private String status;
    private Integer failureCode;
    private String failureComments;
    private Date createdAt;

}

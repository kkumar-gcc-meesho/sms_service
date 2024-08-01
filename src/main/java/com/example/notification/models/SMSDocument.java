package com.example.notification.models;

import com.example.notification.constants.Elasticsearch;
import com.example.notification.enums.SMSStatus;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.UUID;

@Document(indexName = Elasticsearch.INDEX)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSDocument {

    private UUID id;
    private String phoneNumber;
    private String message;

    @Enumerated
    private SMSStatus status;

    private Integer failureCode;
    private String failureComments;
    private Date createdAt;

}

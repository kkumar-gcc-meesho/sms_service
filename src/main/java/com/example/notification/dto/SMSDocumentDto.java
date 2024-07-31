package com.example.notification.dto;

import com.example.notification.enums.SMSStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSDocumentDto {

    private Long id;
    private String phoneNumber;
    private String message;
    private SMSStatus status;
    private Integer failureCode;
    private String failureComments;
    private Date createdAt;

}

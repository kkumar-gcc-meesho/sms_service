package com.example.notification.dto;

import com.example.notification.annotations.PhoneNumber;
import com.example.notification.enums.SMSStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSDto {

    private Long id;

    @PhoneNumber
    private String phoneNumber;

    @NotNull
    private String message;

    @Enumerated
    private SMSStatus status;
    private Integer failureCode;
    private String failureComments;

    private Date createdAt;

}

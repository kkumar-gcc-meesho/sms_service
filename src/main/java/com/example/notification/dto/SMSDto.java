package com.example.notification.dto;

import com.example.notification.annotations.PhoneNumber;
import com.example.notification.constants.Message;
import com.example.notification.enums.SMSStatus;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSDto {

    private UUID id;

    @PhoneNumber
    private String phoneNumber;

    @NotNull(message = Message.ERROR_MESSAGE_MUST_NOT_BE_NULL)
    @NotBlank(message = Message.ERROR_MESSAGE_MUST_NOT_BE_BLANK)
    private String message;

    @Enumerated
    private SMSStatus status;
    private Integer failureCode;
    private String failureComments;

    private Date createdAt;

}

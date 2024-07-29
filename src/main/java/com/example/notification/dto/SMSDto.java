package com.example.notification.dto;

import com.example.notification.enums.SMSStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSDto {

    private Long id;

    @NotBlank(message = "The phone number is required.")
    @Pattern(regexp = "^(\\+91)?[6-9]\\d{9}$", message = "The phone number is not valid.")
    private String phoneNumber;

    @NotNull
    private String message;

    @Builder.Default
    private String status = SMSStatus.PENDING.toString();
    private Integer failureCode;
    private String failureComments;

    private Date createdAt;

}

package com.example.notification.mappers;

import com.example.notification.dto.SMSDto;
import com.example.notification.models.SMS;

public class SMSMapper {

    public static SMSDto toDto(SMS sms){
        return new SMSDto(
                sms.getId(),
                sms.getPhoneNumber(),
                sms.getMessage(),
                sms.getStatus(),
                sms.getFailureCode(),
                sms.getFailureComments(),
                sms.getCreatedAt()
        );
    }

    public static SMS toEntity(SMSDto smsDto){
        SMS sms = new SMS();
        sms.setId(smsDto.getId());
        sms.setPhoneNumber(smsDto.getPhoneNumber());
        sms.setMessage(smsDto.getMessage());
        sms.setStatus(smsDto.getStatus());
        sms.setFailureCode(smsDto.getFailureCode());
        sms.setFailureComments(smsDto.getFailureComments());
        return sms;
    }

}

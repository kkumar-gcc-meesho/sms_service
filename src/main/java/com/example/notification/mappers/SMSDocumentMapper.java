package com.example.notification.mappers;

import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.models.SMSDocument;

public class SMSDocumentMapper {

    public static SMSDocumentDto toDto(SMSDocument smsDocument) {
        return new SMSDocumentDto(
                smsDocument.getId(),
                smsDocument.getPhoneNumber(),
                smsDocument.getMessage(),
                smsDocument.getStatus(),
                smsDocument.getFailureCode(),
                smsDocument.getFailureComments(),
                smsDocument.getCreatedAt()
        );
    }

    public static SMSDocument toEntity(SMSDocumentDto smsDocumentDto) {
        return new SMSDocument(
                smsDocumentDto.getId(),
                smsDocumentDto.getPhoneNumber(),
                smsDocumentDto.getMessage(),
                smsDocumentDto.getStatus(),
                smsDocumentDto.getFailureCode(),
                smsDocumentDto.getFailureComments(),
                smsDocumentDto.getCreatedAt()
        );
    }

}

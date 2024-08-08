package com.example.notification.services.sms;

import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface SMSService {
    SMSDto createSMS(SMSDto smsDto);
    void createSMSDocument(SMSDocumentDto smsDocumentDto);
    SMSDto getSMSById(UUID smsId);
    void updateSMS(UUID smsId, SMSDto updatedSMSDto);
    Page<SMSDocumentDto> getSMSDocumentsByPhoneNumberAndDateRange(String phoneNumber, Date startDate, Date endDate, Pageable pageable);
    Page<SMSDocumentDto> getSMSDocumentsByMessageContaining(String text, Pageable pageable);
}

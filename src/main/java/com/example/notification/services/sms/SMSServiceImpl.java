package com.example.notification.services.sms;

import com.example.notification.constants.Message;
import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.exceptions.ResourceNotFoundException;
import com.example.notification.mappers.SMSDocumentMapper;
import com.example.notification.mappers.SMSMapper;
import com.example.notification.models.SMS;
import com.example.notification.models.SMSDocument;
import com.example.notification.repositories.elastic.SMSElasticRepository;
import com.example.notification.repositories.jpa.SMSJpaRepository;
import com.example.notification.utils.PhoneNumberUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class SMSServiceImpl implements SMSService {

    private final PhoneNumberUtils phoneNumberUtils = new PhoneNumberUtils();

    private final SMSJpaRepository smsJpaRepository;
    private final SMSElasticRepository smsElasticRepository;

    @Override
    public SMSDto createSMS(SMSDto smsDto) {
        SMS sms = SMSMapper.toEntity(smsDto);
        sms.setPhoneNumber(standardizePhoneNumber(sms.getPhoneNumber()));
        SMS savedSMS = smsJpaRepository.save(sms);
        return SMSMapper.toDto(savedSMS);
    }

    @Override
    public void createSMSDocument(SMSDocumentDto smsDocumentDto) {
        SMSDocument smsDocument = SMSDocumentMapper.toEntity(smsDocumentDto);
        smsElasticRepository.save(smsDocument);
    }

    @Override
    public SMSDto getSMSById(Long smsID){
        SMS sms = smsJpaRepository.findById(smsID).orElseThrow(
                () -> new ResourceNotFoundException(Message.ERROR_SMS_NOT_FOUND)
        );

        return SMSMapper.toDto(sms);
    }

    @Override
    public void updateSMS(Long smsId, SMSDto updatedSMSDto){
        SMS sms = smsJpaRepository.findById(smsId).orElseThrow(
                () -> new ResourceNotFoundException(Message.ERROR_SMS_NOT_FOUND)
        );

        sms.setStatus(updatedSMSDto.getStatus());
        sms.setFailureCode(updatedSMSDto.getFailureCode());
        sms.setFailureComments(updatedSMSDto.getFailureComments());

        smsJpaRepository.save(sms);
    }

    @Override
    public Page<SMSDocumentDto> getSMSDocumentsByPhoneNumberAndDateRange(String phoneNumber, Date startDate, Date endDate, Pageable pageable) {
        phoneNumber = standardizePhoneNumber(phoneNumber);

        Page<SMSDocument> smsDocuments;

        if (startDate != null && endDate != null) {
            if (startDate.before(endDate) || startDate.equals(endDate)) {
                smsDocuments = smsElasticRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, startDate, endDate, pageable);
            } else {
                throw new IllegalArgumentException(Message.ERROR_START_DATE_AFTER_END_DATE);
            }
        } else if (startDate == null && endDate != null) {
            smsDocuments = smsElasticRepository.findByPhoneNumberAndCreatedAtBefore(phoneNumber, endDate, pageable);
        } else if (startDate != null) {
            smsDocuments = smsElasticRepository.findByPhoneNumberAndCreatedAtAfter(phoneNumber, startDate, pageable);
        } else {
            smsDocuments = smsElasticRepository.findByPhoneNumber(phoneNumber, pageable);
        }

        return smsDocuments.map(SMSDocumentMapper::toDto);
    }

    @Override
    public Page<SMSDocumentDto> getSMSDocumentsByMessageContaining(String text, Pageable pageable) {
        Page<SMSDocument> smsDocuments = smsElasticRepository.findByMessageContaining(text, pageable);
        return smsDocuments.map(SMSDocumentMapper::toDto);
    }

    private String standardizePhoneNumber(String phoneNumber) {
        phoneNumberUtils.setPhoneNumber(phoneNumber);

        phoneNumber = phoneNumberUtils.getE164Format();

        return phoneNumber;
    }
}

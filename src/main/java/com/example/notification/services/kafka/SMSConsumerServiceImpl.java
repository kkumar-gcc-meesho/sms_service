package com.example.notification.services.kafka;

import com.example.notification.constants.NotificationConstants;
import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.enums.SMSStatus;
import com.example.notification.exceptions.BlacklistedPhoneNumberException;
import com.example.notification.services.SMSSenderPayload;
import com.example.notification.services.SMSSenderResponse;
import com.example.notification.services.SMSSenderService;
import com.example.notification.services.sms.SMSService;
import com.example.notification.services.blacklist.BlacklistService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SMSConsumerServiceImpl implements SMSConsumerService {

    protected static final Logger logger = LogManager.getLogger();

    private final SMSService smsService;
    private final BlacklistService blacklistService;
    private final SMSSenderService smsSenderService;

    @Override
    @KafkaListener(topics = NotificationConstants.KAFKA_TOPIC)
    public void listen(String smsId) throws BlacklistedPhoneNumberException {
        SMSDto smsDto = smsService.getSMSById(Long.valueOf(smsId));

        boolean isBlacklisted = blacklistService.isPhoneNumberBlacklisted(smsDto.getPhoneNumber());
        if (isBlacklisted) {
            throw new BlacklistedPhoneNumberException();
        }

        try {

           ResponseEntity<SMSSenderResponse> response = smsSenderService.send(new SMSSenderPayload(
                    smsDto.getPhoneNumber(),
                    smsDto.getPhoneNumber(),
                    String.valueOf(smsDto.getId())
            ));

           if (response.getStatusCode().is2xxSuccessful()) {
               smsDto.setStatus(SMSStatus.SENT.toString());
           } else {

               logger.error(response.getBody());

               smsDto.setStatus(SMSStatus.FAILED.toString());
               smsDto.setFailureCode(response.getStatusCode().value());
               smsDto.setFailureComments(Objects.requireNonNull(response.getBody()).toString());
           }

        } catch (Exception e) {

            logger.error(e);

            smsDto.setStatus(SMSStatus.FAILED.toString());
            smsDto.setFailureCode(-1);
            smsDto.setFailureComments(e.getMessage());

        } finally {

            SMSDocumentDto smsDoc = new SMSDocumentDto(
                    smsDto.getId(),
                    smsDto.getPhoneNumber(),
                    smsDto.getMessage(),
                    smsDto.getStatus(),
                    smsDto.getFailureCode(),
                    smsDto.getFailureComments(),
                    smsDto.getCreatedAt()
            );

            smsService.createSMSDocument(smsDoc);
            smsService.updateSMS(Long.valueOf(smsId), smsDto);

        }
    }
}

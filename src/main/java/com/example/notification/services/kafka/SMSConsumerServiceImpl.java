package com.example.notification.services.kafka;

import com.example.notification.constants.Kafka;
import com.example.notification.constants.Message;
import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.enums.SMSStatus;
import com.example.notification.exceptions.BlacklistedPhoneNumberException;
import com.example.notification.services.imiconnect.SMSSenderPayload;
import com.example.notification.services.imiconnect.SMSSenderResponse;
import com.example.notification.services.imiconnect.SMSSenderService;
import com.example.notification.services.sms.SMSService;
import com.example.notification.services.blacklist.BlacklistService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SMSConsumerServiceImpl implements SMSConsumerService {

    protected static final Logger logger = LogManager.getLogger();

    private final SMSService smsService;
    private final BlacklistService blacklistService;
    private final SMSSenderService smsSenderService;

    @Override
    @KafkaListener(topics = Kafka.SMS_TOPIC)
    public void listen(String smsId) throws BlacklistedPhoneNumberException {
        SMSDto smsDto = smsService.getSMSById(UUID.fromString(smsId));

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

            if (response != null && response.getStatusCode().is2xxSuccessful()) {

                SMSSenderResponse body = response.getBody();
                if (body != null && body.getResponse().is7xxxError()){

                    smsDto.setStatus(SMSStatus.FAILED);
                    smsDto.setFailureCode(body.getResponse().getCode());
                    smsDto.setFailureComments(body.getResponse().getDescription());

                } else{
                    smsDto.setStatus(SMSStatus.SENT);
                }

            } else {
                smsDto.setStatus(SMSStatus.FAILED);
                smsDto.setFailureCode(response != null ? response.getStatusCode().value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
                smsDto.setFailureComments(response != null && response.getBody() != null ? response.getBody().toString() : Message.ERROR_INTERNAL_SERVER);

                logger.error(response != null ? response.getBody() : "Response is null");
            }

        } catch (Exception e) {

            smsDto.setStatus(SMSStatus.FAILED);
            smsDto.setFailureCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            smsDto.setFailureComments(e.getMessage());

            logger.error("Exception occurred while sending SMS", e);

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
            smsService.updateSMS(UUID.fromString(smsId), smsDto);

        }
    }
}

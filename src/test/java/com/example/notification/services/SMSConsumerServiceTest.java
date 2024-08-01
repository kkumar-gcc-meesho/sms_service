package com.example.notification.services;

import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.enums.SMSStatus;
import com.example.notification.exceptions.BlacklistedPhoneNumberException;
import com.example.notification.services.imiconnect.SMSSenderPayload;
import com.example.notification.services.imiconnect.SMSSenderService;
import com.example.notification.services.kafka.SMSConsumerServiceImpl;
import com.example.notification.services.sms.SMSService;
import com.example.notification.services.blacklist.BlacklistService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class SMSConsumerServiceTest {

    @Mock
    private SMSService smsService;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private SMSSenderService smsSenderService;

    @InjectMocks
    private SMSConsumerServiceImpl smsConsumerService;

    @Test
    public void listenShouldSendSMSWhenPhoneNumberIsNotBlacklisted() {
        SMSDto smsDto = getTestSMSDto();

        when(smsService.getSMSById(smsDto.getId())).thenReturn(smsDto);
        when(blacklistService.isPhoneNumberBlacklisted(smsDto.getPhoneNumber())).thenReturn(false);

        smsConsumerService.listen(String.valueOf(smsDto.getId()));

        verify(smsService, times(1)).getSMSById(smsDto.getId());
        verify(blacklistService, times(1)).isPhoneNumberBlacklisted(smsDto.getPhoneNumber());
        verify(smsSenderService, times(1)).send(any(SMSSenderPayload.class));

        ArgumentCaptor<SMSDto> smsDtoCaptor = ArgumentCaptor.forClass(SMSDto.class);
        verify(smsService, times(1)).updateSMS(eq(smsDto.getId()), smsDtoCaptor.capture());
        SMSDto updatedSMSDto = smsDtoCaptor.getValue();

        assertThat(updatedSMSDto.getStatus()).isEqualTo(SMSStatus.FAILED);

        verify(smsService, times(1)).createSMSDocument(any(SMSDocumentDto.class));
    }

    @Test
    public void listenShouldThrowBlacklistedPhoneNumberExceptionWhenPhoneNumberIsBlacklisted(){
        SMSDto smsDto = getTestSMSDto();

        when(smsService.getSMSById(smsDto.getId())).thenReturn(smsDto);
        when(blacklistService.isPhoneNumberBlacklisted(smsDto.getPhoneNumber())).thenReturn(true);

        assertThrows(BlacklistedPhoneNumberException.class, () -> smsConsumerService.listen(String.valueOf(smsDto.getId())));

        verify(smsService, times(1)).getSMSById(smsDto.getId());
        verify(blacklistService, times(1)).isPhoneNumberBlacklisted(smsDto.getPhoneNumber());
        verify(smsSenderService, never()).send(any(SMSSenderPayload.class));
        verify(smsService, never()).updateSMS(anyLong(), any(SMSDto.class));
        verify(smsService, never()).createSMSDocument(any(SMSDocumentDto.class));
    }

    @Test
    public void listenShouldUpdateSMSStatusToFailedWhenSMSSendingFails() {
        SMSDto smsDto = getTestSMSDto();

        when(smsService.getSMSById(smsDto.getId())).thenReturn(smsDto);
        when(blacklistService.isPhoneNumberBlacklisted(smsDto.getPhoneNumber())).thenReturn(false);
        doThrow(new RuntimeException("Sending Failed")).when(smsSenderService).send(any(SMSSenderPayload.class));

        smsConsumerService.listen(String.valueOf(smsDto.getId()));

        verify(smsService, times(1)).getSMSById(smsDto.getId());
        verify(blacklistService, times(1)).isPhoneNumberBlacklisted(smsDto.getPhoneNumber());
        verify(smsSenderService, times(1)).send(any(SMSSenderPayload.class));

        ArgumentCaptor<SMSDto> smsDtoCaptor = ArgumentCaptor.forClass(SMSDto.class);
        verify(smsService, times(1)).updateSMS(eq(smsDto.getId()), smsDtoCaptor.capture());
        SMSDto updatedSMSDto = smsDtoCaptor.getValue();

        assertThat(updatedSMSDto.getStatus()).isEqualTo(SMSStatus.FAILED);
        assertThat(updatedSMSDto.getFailureCode()).isEqualTo(520);
        assertThat(updatedSMSDto.getFailureComments()).isEqualTo("Sending Failed");

        verify(smsService, times(1)).createSMSDocument(any(SMSDocumentDto.class));
    }

    private SMSDto getTestSMSDto(){
        SMSDto smsDto = new SMSDto();
        smsDto.setId(1L);
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Test Message");
        smsDto.setCreatedAt(new Date());

        return smsDto;
    }
}

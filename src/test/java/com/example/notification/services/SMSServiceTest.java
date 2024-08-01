package com.example.notification.services;

import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.exceptions.ResourceNotFoundException;
import com.example.notification.mappers.SMSMapper;
import com.example.notification.models.SMS;
import com.example.notification.models.SMSDocument;
import com.example.notification.repositories.elastic.SMSElasticRepository;
import com.example.notification.repositories.jpa.SMSJpaRepository;
import com.example.notification.services.sms.SMSServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class SMSServiceTest {

    @Mock
    private SMSJpaRepository smsJpaRepository;

    @Mock
    private SMSElasticRepository smsElasticRepository;

    @InjectMocks
    private SMSServiceImpl smsServiceImpl;

    @Test
    public void createSMS() {
        UUID smsId = UUID.randomUUID();

        SMSDto smsDto = new SMSDto();
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Hello World");

        SMS sms = SMSMapper.toEntity(smsDto);
        sms.setId(smsId);

        when(smsJpaRepository.save(any(SMS.class))).thenReturn(sms);

        SMSDto result = smsServiceImpl.createSMS(smsDto);

        verify(smsJpaRepository, times(1)).save(any(SMS.class));

        assertThat(result).isNotNull();
        assertThat(result.getPhoneNumber()).isEqualTo("+917986543210");
        assertThat(result.getMessage()).isEqualTo("Hello World");
        assertThat(result.getId()).isEqualTo(smsId);
    }

    @Test
    public void getSMSById(){
        UUID smsId = UUID.randomUUID();

        SMS sms = new SMS();
        sms.setId(smsId);
        sms.setPhoneNumber("+917986543210");
        sms.setMessage("Hello World");

        when(smsJpaRepository.findById(smsId)).thenReturn(Optional.of(sms));

        SMSDto smsDto = smsServiceImpl.getSMSById(smsId);

        verify(smsJpaRepository, times(1)).findById(smsId);
        assertThat(smsDto).isNotNull();
        assertThat(smsDto.getId()).isEqualTo(smsId);
        assertThat(smsDto.getPhoneNumber()).isEqualTo("+917986543210");
        assertThat(smsDto.getMessage()).isEqualTo("Hello World");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getSMSByIdShouldThrowResourceNotFoundException(){
        UUID smsId = UUID.randomUUID();

        when(smsJpaRepository.findById(smsId)).thenReturn(Optional.empty());
        smsServiceImpl.getSMSById(smsId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateSMSShouldThrowResourceNotFoundException(){
        UUID smsId = UUID.randomUUID();
        SMSDto smsDto = new SMSDto();
        smsDto.setId(smsId);
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Hello World!");

        when(smsJpaRepository.findById(smsId)).thenReturn(Optional.empty());
        smsServiceImpl.updateSMS(smsId, smsDto);
    }

    @Test
    public void getSMSDocumentsByPhoneNumberAndDateRange() {
        String phoneNumber = "+917986543210";
        Date startDate = new Date(System.currentTimeMillis() - 100000);
        Date endDate = new Date();
        Pageable pageable = PageRequest.of(0, 10);

        Page<SMSDocument> smsDocumentPage = getSmsDocuments(phoneNumber);

        when(smsElasticRepository.findByPhoneNumberAndCreatedAtBetween(phoneNumber, startDate, endDate, pageable))
                .thenReturn(smsDocumentPage);

        Page<SMSDocumentDto> result = smsServiceImpl.getSMSDocumentsByPhoneNumberAndDateRange(phoneNumber, startDate, endDate, pageable);

        verify(smsElasticRepository, times(1)).findByPhoneNumberAndCreatedAtBetween(phoneNumber, startDate, endDate, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.getContent().get(0).getMessage()).isEqualTo("Message 1");
        assertThat(result.getContent().get(1).getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.getContent().get(1).getMessage()).isEqualTo("Message 2");
    }

    @Test
    public void getSMSDocumentsByMessageContaining() {
        String text = "Message";
        String phoneNumber = "+917986543210";
        Pageable pageable = PageRequest.of(0, 10);

        Page<SMSDocument> smsDocumentPage = getSmsDocuments(phoneNumber);

        when(smsElasticRepository.findByMessageContaining(text, pageable)).thenReturn(smsDocumentPage);

        Page<SMSDocumentDto> result = smsServiceImpl.getSMSDocumentsByMessageContaining(text, pageable);

        verify(smsElasticRepository, times(1)).findByMessageContaining(text, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getMessage()).isEqualTo("Message 1");
        assertThat(result.getContent().get(1).getMessage()).isEqualTo("Message 2");
    }

    private static Page<SMSDocument> getSmsDocuments(String phoneNumber) {
        SMSDocument smsDocument1 = new SMSDocument();
        smsDocument1.setId(UUID.randomUUID());
        smsDocument1.setPhoneNumber(phoneNumber);
        smsDocument1.setMessage("Message 1");

        SMSDocument smsDocument2 = new SMSDocument();
        smsDocument2.setId(UUID.randomUUID());
        smsDocument2.setPhoneNumber(phoneNumber);
        smsDocument2.setMessage("Message 2");

        List<SMSDocument> smsDocumentList = Arrays.asList(smsDocument1, smsDocument2);
        return new PageImpl<>(smsDocumentList);
    }

}

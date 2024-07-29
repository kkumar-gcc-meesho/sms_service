package com.example.notification.services;

import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.exceptions.ResourceNotFoundException;
import com.example.notification.mappers.SMSDocumentMapper;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        SMSDto smsDto = new SMSDto();
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Hello World");

        SMS sms = SMSMapper.toEntity(smsDto);
        sms.setId(1L);

        when(smsJpaRepository.save(any(SMS.class))).thenReturn(sms);

        SMSDto result = smsServiceImpl.createSMS(smsDto);

        verify(smsJpaRepository, times(1)).save(any(SMS.class));

        assertThat(result).isNotNull();
        assertThat(result.getPhoneNumber()).isEqualTo("+917986543210");
        assertThat(result.getMessage()).isEqualTo("Hello World");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void createSMSDocument() {
        SMSDocumentDto smsDocumentDto = new SMSDocumentDto();
        smsDocumentDto.setPhoneNumber("+917986543210");
        smsDocumentDto.setMessage("Hello Document");

        SMSDocument smsDocument = SMSDocumentMapper.toEntity(smsDocumentDto);
        smsDocument.setId(1L);

        when(smsElasticRepository.save(any(SMSDocument.class))).thenReturn(smsDocument);

        SMSDocumentDto result = smsServiceImpl.createSMSDocument(smsDocumentDto);

        verify(smsElasticRepository, times(1)).save(any(SMSDocument.class));

        assertThat(result).isNotNull();
        assertThat(result.getPhoneNumber()).isEqualTo("+917986543210");
        assertThat(result.getMessage()).isEqualTo("Hello Document");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void getSMSById(){
        Long smsId = 1L;

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
        Long smsId = 1L;

        when(smsJpaRepository.findById(smsId)).thenReturn(Optional.empty());
        smsServiceImpl.getSMSById(smsId);
    }

    @Test
    public void updateSMS() {
        Long smsId = 1L;

        SMSDto smsDto = new SMSDto();
        smsDto.setId(smsId);
        smsDto.setPhoneNumber("+917986543210");
        smsDto.setMessage("Hello World!");
        smsDto.setStatus("SENT");

        SMS sms = SMSMapper.toEntity(smsDto);
        sms.setId(1L);
        sms.setPhoneNumber("+917289839849");
        sms.setMessage("Hello Krishan!");

        when(smsJpaRepository.findById(smsId)).thenReturn(Optional.of(sms));
        when(smsJpaRepository.save(any(SMS.class))).thenReturn(SMSMapper.toEntity(smsDto));

        SMSDto result = smsServiceImpl.updateSMS(smsId, smsDto);

        verify(smsJpaRepository, times(1)).findById(smsId);
        verify(smsJpaRepository, times(1)).save(any(SMS.class));
        assertThat(result).isNotNull();
        assertThat(result.getPhoneNumber()).isEqualTo("+917986543210");
        assertThat(result.getMessage()).isEqualTo("Hello World!");
        assertThat(result.getStatus()).isEqualTo("SENT");
        assertThat(result.getFailureCode()).isNull();
        assertThat(result.getFailureComments()).isNull();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateSMSShouldThrowResourceNotFoundException(){
        Long smsId = 1L;
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
        smsDocument1.setId(1L);
        smsDocument1.setPhoneNumber(phoneNumber);
        smsDocument1.setMessage("Message 1");

        SMSDocument smsDocument2 = new SMSDocument();
        smsDocument2.setId(2L);
        smsDocument2.setPhoneNumber(phoneNumber);
        smsDocument2.setMessage("Message 2");

        List<SMSDocument> smsDocumentList = Arrays.asList(smsDocument1, smsDocument2);
        return new PageImpl<>(smsDocumentList);
    }

}

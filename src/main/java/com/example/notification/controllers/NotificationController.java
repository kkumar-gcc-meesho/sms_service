package com.example.notification.controllers;

import com.example.notification.annotations.AuthorizationHeader;
import com.example.notification.constants.Message;
import com.example.notification.constants.Status;
import com.example.notification.dto.BlacklistDto;
import com.example.notification.dto.SMSDocumentDto;
import com.example.notification.dto.SMSDto;
import com.example.notification.responses.*;
import com.example.notification.services.sms.SMSService;
import com.example.notification.services.kafka.SMSProducerService;
import com.example.notification.services.blacklist.BlacklistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping(path = "${apiVersion}")
@AllArgsConstructor
public class NotificationController {

    private final BlacklistService blacklistService;
    private final SMSService smsService;
    private final SMSProducerService smsProducerService;

    @PostMapping("/sms/send")
    @AuthorizationHeader
    public ApiResponse<SendSMSData> sendSMS(@Valid @RequestBody SMSDto smsDto) {
        UUID smsId = smsProducerService.send(smsDto);

        return ApiResponse.<SendSMSData>builder()
                .status(Status.SUCCESS)
                .data(new SendSMSData(smsId, Message.SUCCESS_SMS_SENT))
                .build();
    }

    @PostMapping("/blacklist")
    @AuthorizationHeader
    public ApiResponse<String> addToBlacklist(@Valid @RequestBody BlacklistDto blacklistDto) {
        blacklistService.addPhoneNumbersToBlacklist(blacklistDto);

        return ApiResponse.<String>builder()
                .status(Status.SUCCESS)
                .data(Message.SUCCESS_PHONE_NUMBER_BLACKLISTED)
                .build();
    }

    @DeleteMapping("/blacklist")
    @AuthorizationHeader
    public ApiResponse<String> removeFromBlacklist(@Valid @RequestBody BlacklistDto blacklistDto) {
        blacklistService.removePhoneNumbersFromBlacklist(blacklistDto);

        return ApiResponse.<String>builder()
                .status(Status.SUCCESS)
                .data(Message.SUCCESS_PHONE_NUMBER_WHITELISTED)
                .build();
    }

    @GetMapping("/blacklist")
    @AuthorizationHeader
    public ApiResponse<Set<String>> getAllBlacklistedPhoneNumbers() {
        return ApiResponse.<Set<String>>builder()
                .status(Status.SUCCESS)
                .data(blacklistService.getAllBlacklistedPhoneNumbers())
                .build();
    }

    @GetMapping("/sms/{requestId}")
    @AuthorizationHeader
    public ApiResponse<SMSDto> getSMSById(@PathVariable("requestId") UUID smsId) {
        return ApiResponse.<SMSDto>builder()
                .status(Status.SUCCESS)
                .data(smsService.getSMSById(smsId))
                .build();
    }

    @GetMapping("/sms/search/phone")
    @AuthorizationHeader
    public Page<SMSDocumentDto> searchSMSDocumentsByPhoneNumberAndDateRange(
            @RequestParam String phoneNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            Pageable pageable) {
        return smsService.getSMSDocumentsByPhoneNumberAndDateRange(phoneNumber, startDate, endDate, pageable);
    }

    @GetMapping("/sms/search/message")
    @AuthorizationHeader
    public Page<SMSDocumentDto> searchSMSDocumentsByMessageContaining(
            @RequestParam String message,
            Pageable pageable) {
        return smsService.getSMSDocumentsByMessageContaining(message, pageable);
    }

}
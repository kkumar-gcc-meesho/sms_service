package com.example.notification.services.blacklist;

import com.example.notification.dto.BlacklistDto;

import java.util.Set;

public interface BlacklistService {
    void addPhoneNumbersToBlacklist(BlacklistDto blacklistDto);
    void removePhoneNumbersFromBlacklist(BlacklistDto blacklistDto);
    boolean isPhoneNumberBlacklisted(String phoneNumber);
    Set<String> getAllBlacklistedPhoneNumbers();
}

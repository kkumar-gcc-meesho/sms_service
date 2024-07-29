package com.example.notification.mappers;

import com.example.notification.dto.BlacklistDto;
import com.example.notification.models.Blacklist;

import java.util.List;
import java.util.stream.Collectors;

public class BlacklistMapper {

    public static List<Blacklist> toEntities(BlacklistDto dto) {
        return dto.getPhoneNumbers().stream()
                .map(phoneNumber -> {
                    Blacklist blacklist = new Blacklist();
                    blacklist.setPhoneNumber(phoneNumber);
                    return blacklist;
                })
                .collect(Collectors.toList());
    }

    public static BlacklistDto toDto(List<Blacklist> blacklistEntities) {
        List<String> phoneNumbers = blacklistEntities.stream()
                .map(Blacklist::getPhoneNumber)
                .collect(Collectors.toList());
        return BlacklistDto.builder()
                .phoneNumbers(phoneNumbers)
                .build();
    }

}

package com.example.notification.services.blacklist;

import com.example.notification.dto.BlacklistDto;
import com.example.notification.mappers.BlacklistMapper;
import com.example.notification.models.Blacklist;
import com.example.notification.repositories.jpa.BlacklistJpaRepository;
import com.example.notification.utils.PhoneNumberUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class BlacklistServiceImpl implements BlacklistService {

    private static final String BLACKLIST_KEY = "sms:phoneNumber:blacklist";
    private final PhoneNumberUtils phoneNumberUtils = new PhoneNumberUtils();

    private final JedisPooled jedis;
    private final BlacklistJpaRepository blacklistJpaRepository;

    @Override
    public void addPhoneNumbersToBlacklist(BlacklistDto blacklistDto) {
        List<Blacklist> blacklistEntities = BlacklistMapper.toEntities(blacklistDto).stream()
                .peek(blacklist -> {
                    String standardizedNumber = standardizePhoneNumber(blacklist.getPhoneNumber());
                    blacklist.setPhoneNumber(standardizedNumber);
                })
                .collect(Collectors.toList());

        blacklistJpaRepository.saveAll(blacklistEntities);

        for (String phoneNumber : blacklistDto.getPhoneNumbers()) {
            String standardizedNumber = standardizePhoneNumber(phoneNumber);
            jedis.sadd(BLACKLIST_KEY, standardizedNumber);
        }
    }

    @Override
    public void removePhoneNumbersFromBlacklist(BlacklistDto blacklistDto) {
        for (String phoneNumber : blacklistDto.getPhoneNumbers()) {
            String standardizedNumber = standardizePhoneNumber(phoneNumber);
            jedis.srem(BLACKLIST_KEY, standardizedNumber);
        }

        List<String> standardizedPhoneNumbers = blacklistDto.getPhoneNumbers().stream()
                .map(this::standardizePhoneNumber)
                .toList();

        blacklistJpaRepository.removeBlacklistsByPhoneNumberIn(standardizedPhoneNumbers);
    }

    @Override
    public boolean isPhoneNumberBlacklisted(String phoneNumber) {
        String standardizedNumber = standardizePhoneNumber(phoneNumber);
        if (jedis.sismember(BLACKLIST_KEY, standardizedNumber)) {
            return true;
        }
        return blacklistJpaRepository.existsByPhoneNumber(standardizedNumber);
    }

    @Override
    public Set<String> getAllBlacklistedPhoneNumbers() {
        Set<String> phoneNumbers = jedis.smembers(BLACKLIST_KEY);
        if (phoneNumbers.isEmpty()) {
            List<Blacklist> entities = blacklistJpaRepository.findAll();
            phoneNumbers = entities.stream()
                    .map(Blacklist::getPhoneNumber)
                    .collect(Collectors.toSet());
            jedis.sadd(BLACKLIST_KEY, phoneNumbers.toArray(new String[0]));
        }
        return phoneNumbers;
    }

    private String standardizePhoneNumber(String phoneNumber) {
        phoneNumberUtils.setPhoneNumber(phoneNumber);

        phoneNumber = phoneNumberUtils.getE164Format();

        phoneNumberUtils.resetPhoneNumber();

        return phoneNumber;
    }
}
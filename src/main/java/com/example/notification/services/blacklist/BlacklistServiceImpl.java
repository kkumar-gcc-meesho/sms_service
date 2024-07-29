package com.example.notification.services.blacklist;

import com.example.notification.dto.BlacklistDto;
import com.example.notification.mappers.BlacklistMapper;
import com.example.notification.models.Blacklist;
import com.example.notification.repositories.jpa.BlacklistJpaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class BlacklistServiceImpl implements BlacklistService {

    private static final String BLACKLIST_KEY = "sms:phoneNumber:blacklist";
    private static final String INDIAN_COUNTRY_CODE = "+91";
    private static final Pattern INTERNATIONAL_PHONE_PATTERN = Pattern.compile("^\\+\\d+");

    private final JedisPooled jedis;
    private final BlacklistJpaRepository blacklistJpaRepository;

    @Override
    public void addPhoneNumbersToBlacklist(BlacklistDto blacklistDto) {
        List<Blacklist> blacklistEntities = BlacklistMapper.toEntities(blacklistDto);
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
        blacklistJpaRepository.removeBlacklistsByPhoneNumberIn(blacklistDto.getPhoneNumbers());
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
        phoneNumber = phoneNumber.trim();
        Matcher matcher = INTERNATIONAL_PHONE_PATTERN.matcher(phoneNumber);
        if (matcher.find()) {
            return phoneNumber;
        }

        return INDIAN_COUNTRY_CODE + phoneNumber;
    }
}
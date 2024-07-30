package com.example.notification.services;

import com.example.notification.config.SMSConfig;
import com.example.notification.dto.BlacklistDto;
import com.example.notification.models.Blacklist;
import com.example.notification.repositories.jpa.BlacklistJpaRepository;
import com.example.notification.services.blacklist.BlacklistServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class BlacklistServiceTest {

    private static final String BLACKLIST_KEY = "sms:phoneNumber:blacklist";

    @Mock
    private BlacklistJpaRepository blacklistJpaRepository;

    @Mock
    JedisPooled jedis;

    @Mock
    private SMSConfig smsConfig;

    @InjectMocks
    private BlacklistServiceImpl blacklistServiceImpl;

    @Test
    public void addPhoneNumberToBlacklist() {
        BlacklistDto blacklistDto = new BlacklistDto();
        blacklistDto.setPhoneNumbers(List.of("+917986543210", "7986543210"));

        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);
        when(blacklistJpaRepository.saveAll(anyIterable())).thenReturn(any());

        blacklistServiceImpl.addPhoneNumbersToBlacklist(blacklistDto);

        verify(blacklistJpaRepository, times(1)).saveAll(anyIterable());
        verify(jedis, times(2)).sadd(eq(BLACKLIST_KEY), eq("+917986543210"));
    }

    @Test
    public void removePhoneNumberFromBlacklist() {
        BlacklistDto blacklistDto = new BlacklistDto();
        blacklistDto.setPhoneNumbers(List.of("+917986543210"));

        when(jedis.srem(eq(BLACKLIST_KEY), eq("+917986543210"))).thenReturn(1L);
        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);
        doNothing().when(blacklistJpaRepository).removeBlacklistsByPhoneNumberIn(blacklistDto.getPhoneNumbers());

        blacklistServiceImpl.removePhoneNumbersFromBlacklist(blacklistDto);

        verify(blacklistJpaRepository, times(1)).removeBlacklistsByPhoneNumberIn(blacklistDto.getPhoneNumbers());
        verify(jedis, times(1)).srem(eq(BLACKLIST_KEY), eq("+917986543210"));
    }

    @Test
    public void checkIfPhoneNumberIsBlocklistedFromRedis() {
        String phoneNumber = "+917986543210";

        when(jedis.sismember(eq(BLACKLIST_KEY), eq(phoneNumber))).thenReturn(true);
        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);

        boolean isBlacklisted = blacklistServiceImpl.isPhoneNumberBlacklisted(phoneNumber);

        assertTrue(isBlacklisted);

        verify(jedis, times(1)).sismember(eq(BLACKLIST_KEY), eq(phoneNumber));
    }

    @Test
    public void checkIfPhoneNumberIsNotBlocklistedFromDb() {
        String phoneNumber = "+917986543210";

        when(jedis.sismember(eq(BLACKLIST_KEY), eq(phoneNumber))).thenReturn(false);
        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);
        when(blacklistJpaRepository.existsByPhoneNumber(eq(phoneNumber))).thenReturn(true);

        boolean isBlacklisted = blacklistServiceImpl.isPhoneNumberBlacklisted(phoneNumber);

        assertTrue(isBlacklisted);

        verify(jedis, times(1)).sismember(eq(BLACKLIST_KEY), eq(phoneNumber));

    }

    @Test
    public void getAllBlacklistedPhoneNumbersFromRedis(){
        Set<String> phoneNumbers = Set.of("+917986543210");

        when(jedis.smembers(BLACKLIST_KEY)).thenReturn(phoneNumbers);
        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);

        assertEquals(phoneNumbers, blacklistServiceImpl.getAllBlacklistedPhoneNumbers());

        verify(jedis, times(1)).smembers(BLACKLIST_KEY);
    }


    @Test
    public void getAllBlacklistedPhoneNumbersFromDbAndCacheInRedis(){
        List<Blacklist> blacklists = new ArrayList<>();
        blacklists.add(new Blacklist(1L, "+917986543210"));

        when(jedis.smembers(BLACKLIST_KEY)).thenReturn(Set.of());
        when(smsConfig.getRedisBlacklistKey()).thenReturn(BLACKLIST_KEY);
        when(blacklistJpaRepository.findAll()).thenReturn(blacklists);

        assertEquals(Set.of("+917986543210"), blacklistServiceImpl.getAllBlacklistedPhoneNumbers());

        verify(jedis, times(1)).smembers(BLACKLIST_KEY);
        verify(blacklistJpaRepository, times(1)).findAll();
    }


}

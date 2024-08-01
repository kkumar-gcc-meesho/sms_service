package com.example.notification.repositories.jpa;

import com.example.notification.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlacklistJpaRepository extends JpaRepository<Blacklist, UUID> {
    void removeBlacklistsByPhoneNumberIn(List<String> phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}

package com.example.notification.repositories.jpa;

import com.example.notification.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistJpaRepository extends JpaRepository<Blacklist, Long> {
    void removeBlacklistsByPhoneNumberIn(List<String> phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}

package com.example.notification.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notification.models.SMS;

import java.util.UUID;

@Repository
public interface SMSJpaRepository extends JpaRepository<SMS, UUID> {}

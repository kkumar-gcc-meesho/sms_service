package com.example.notification.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notification.models.SMS;

@Repository
public interface SMSJpaRepository extends JpaRepository<SMS, Long> {}

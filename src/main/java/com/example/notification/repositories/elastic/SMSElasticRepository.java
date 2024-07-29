package com.example.notification.repositories.elastic;

import com.example.notification.models.SMSDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SMSElasticRepository extends ElasticsearchRepository<SMSDocument, Long> {
    Page<SMSDocument> findByPhoneNumber(String phoneNumber, Pageable pageable);

    Page<SMSDocument> findByPhoneNumberAndCreatedAtAfter(String phoneNumber, Date startDate, Pageable pageable);

    Page<SMSDocument> findByPhoneNumberAndCreatedAtBefore(String phoneNumber, Date endDate, Pageable pageable);

    Page<SMSDocument> findByPhoneNumberAndCreatedAtBetween(String phoneNumber, Date startDate, Date endDate, Pageable pageable);

    Page<SMSDocument> findByMessageContaining(String message, Pageable pageable);
}

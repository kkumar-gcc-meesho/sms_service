package com.example.notification.repositories.elastic;

import com.example.notification.models.SMSDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface SMSElasticRepository extends ElasticsearchRepository<SMSDocument, UUID> {

    Page<SMSDocument> findByPhoneNumberAndCreatedAtIsBetween(String phoneNumber, Date startDate, Date endDate, Pageable pageable);

    Page<SMSDocument> findByMessageContaining(String message, Pageable pageable);

}

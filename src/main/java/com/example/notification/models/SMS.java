package com.example.notification.models;

import com.example.notification.enums.SMSStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SMS implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    private String phoneNumber;
    private String message;

    @Enumerated
    private SMSStatus status;
    private Integer failureCode;
    private String failureComments;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Override
    public String toString() {
        return "SMSRequest{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}

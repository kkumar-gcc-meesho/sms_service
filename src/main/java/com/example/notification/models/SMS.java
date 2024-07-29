package com.example.notification.models;

import com.example.notification.enums.SMSStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SMS {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String phoneNumber;
    private String message;

    @Builder.Default
    private String status = SMSStatus.PENDING.toString();
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

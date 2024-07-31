package com.example.notification.dto;

import com.example.notification.annotations.PhoneNumber;
import com.example.notification.constants.Message;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistDto {

    @NotEmpty(message = Message.ERROR_PHONE_NUMBERS_LIST_EMPTY)
    @Size(min = 1, message = Message.ERROR_PHONE_NUMBERS_LIST_MIN_SIZE)
    private List<@PhoneNumber String> phoneNumbers;

}

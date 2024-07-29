package com.example.notification.dto;

import com.example.notification.annotations.PhoneNumber;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistDto {

    @NotEmpty(message = "Phone numbers list cannot be empty")
    @Size(min = 1, message = "At least one phone number is required")
    private List<@PhoneNumber String> phoneNumbers;

}

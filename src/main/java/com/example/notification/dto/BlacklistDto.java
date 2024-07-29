package com.example.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    private List<
            @NotBlank(message = "The phone number is required.")
            @Pattern(regexp = "^(\\+91)?[6-9]\\d{9}$", message = "The phone number is not valid.")
                    String> phoneNumbers;

}

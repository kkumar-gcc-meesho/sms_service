package com.example.notification.services.imiconnect;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody {
    private int code;
    private String description;
    private String transactionId;

    public boolean is7xxxError(){
        return code >= 7000;
    }
}

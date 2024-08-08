package com.example.notification.services.imiconnect;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class SMSSenderPayload {
    private String phoneNumber;
    private String message;
    private String correlationId;

    private final String SMS_DELIVERY_CHANNEL = "sms";

    public SMSSenderPayload(String phoneNumber, String message, String correlationId) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.correlationId = correlationId;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("deliverychannel", SMS_DELIVERY_CHANNEL)
                .put("channels", createChannelsJson())
                .put("destination", createDestinationJson());
    }

    private JSONObject createChannelsJson() {
        return new JSONObject()
                .put("sms", new JSONObject().put("text", message));
    }

    private JSONArray createDestinationJson() {
        JSONObject destination = new JSONObject()
                .put("msisdn", new JSONArray().put(phoneNumber))
                .put("correlationId", correlationId);

        return new JSONArray().put(destination);
    }

}

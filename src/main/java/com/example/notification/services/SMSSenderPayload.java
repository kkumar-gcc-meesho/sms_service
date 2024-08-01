package com.example.notification.services;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class SMSSenderPayload {
    private String phoneNumber;
    private String message;
    private String correlationId;

    private final String deliveryChannel = "sms";

    public SMSSenderPayload(String phoneNumber, String message, String correlationId) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.correlationId = correlationId;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deliverychannel", deliveryChannel);

        JSONObject channels = new JSONObject();
        JSONObject sms = new JSONObject();
        sms.put("text", message);
        channels.put("sms", sms);

        jsonObject.put("channels", channels);

        JSONObject destination = new JSONObject();
        destination.put("msisdn", new JSONArray().put(phoneNumber));
        destination.put("correlationId", correlationId);

        jsonObject.put("destination", new JSONArray().put(destination));

        return jsonObject;
    }

}

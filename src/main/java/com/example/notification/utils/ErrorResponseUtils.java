package com.example.notification.utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseUtils {
    public static Map<String, Object> add(String statusCode, String message) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("code", statusCode);
        errors.put("message", message);
        return errors;
    }
}

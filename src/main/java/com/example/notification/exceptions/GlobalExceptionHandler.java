package com.example.notification.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.notification.responses.ApiResponse;
import com.example.notification.responses.ErrorResponse;
import com.example.notification.utils.ErrorResponseUtil;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                                                                  HttpStatusCode status, @NonNull WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult()
//                .getFieldErrors()
//                .forEach(error -> {
//                    errors.put(error.getField(), error.getDefaultMessage());
//                });

//        body.put("error", errors);

//        Map<String, Object> body = new HashMap<>();
//        body.put("error", ErrorResponseUtil.add(status.toString(), Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()));
//
//        return new ResponseEntity<>(body, HttpStatusCode.valueOf(400));

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status("error")
                .error(new ErrorResponse(status.toString(), Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(), "Validation failed"))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(BlacklistedPhoneNumberException.class)
    public ResponseEntity<ApiResponse<String>> handleBlacklistedPhoneNumberException(BlacklistedPhoneNumberException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status("error")
                .error(new ErrorResponse("BLACKLISTED_PHONE", ex.getMessage(), "Phone number is blacklisted"))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(HeaderAuthorizationException.class)
    public ResponseEntity<ApiResponse<String>> handleHeaderAuthorizationException(HeaderAuthorizationException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status("error")
                .error(new ErrorResponse(HttpStatus.FORBIDDEN.toString(), ex.getMessage(), "Forbidden"))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSMSNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ErrorResponseUtil.add(String.valueOf(HttpStatus.NOT_FOUND), ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
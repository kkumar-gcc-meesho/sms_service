package com.example.notification.exceptions;

import java.util.Objects;

import com.example.notification.constants.Code;
import com.example.notification.constants.Status;
import com.example.notification.responses.ApiResponse;
import com.example.notification.responses.ErrorResponse;
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
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(status.toString(), Objects.requireNonNull(ex.getFieldError()).getDefaultMessage()))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(BlacklistedPhoneNumberException.class)
    public ResponseEntity<ApiResponse<String>> handleBlacklistedPhoneNumberException(BlacklistedPhoneNumberException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(Code.BLACKLISTED_PHONE, ex.getMessage()))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(HeaderAuthorizationException.class)
    public ResponseEntity<ApiResponse<String>> handleHeaderAuthorizationException(HeaderAuthorizationException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(HttpStatus.FORBIDDEN.toString(), ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleSMSNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidPhoneNumberException(InvalidPhoneNumberException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(Status.ERROR)
                .error(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

}
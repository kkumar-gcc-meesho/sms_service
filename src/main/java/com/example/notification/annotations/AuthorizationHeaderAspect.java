package com.example.notification.annotations;

import com.example.notification.constants.Key;
import com.example.notification.constants.Message;
import com.example.notification.exceptions.HeaderAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationHeaderAspect {

    private final @NonNull HttpServletRequest request;

    @Before("@annotation(AuthorizationHeader)")
    public void before() {
          String header = request.getHeader(Key.AUTHORIZATION_HEADER);

          if (header == null || header.isBlank() || !header.startsWith("Bearer ")){
              throw new HeaderAuthorizationException(Message.ERROR_AUTHORIZATION_HEADER_EMPTY);
          }
    }

}

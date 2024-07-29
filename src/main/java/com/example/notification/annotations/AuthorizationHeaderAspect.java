package com.example.notification.annotations;

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

    private static final String AUTHORIZATION = "Authorization";

    private final @NonNull HttpServletRequest request;

    @Before("@annotation(AuthorizationHeader)")
    public void before() {
          String header = request.getHeader(AUTHORIZATION);

          if (header == null || header.isBlank() || !header.startsWith("Bearer ")){
              throw new HeaderAuthorizationException("Authorization header is empty");
          }
    }

}

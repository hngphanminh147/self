package com.self.uaa.helper;

import com.self.uaa.model.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                exception.getLocalizedMessage(),
                null,
                HttpStatus.UNAUTHORIZED,
                request);
    }

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(ApiError exception, WebRequest request) {
        return handleExceptionInternal(
                exception,
                exception,
                null,
                HttpStatus.valueOf(exception.getStatus().getStatusCode()),
                request);
    }
}


package com.lifekhmer.common.response;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/** Keeps error payloads identical for MVC advice and Spring Security handlers. */
@Component
public class ApiErrorResponseFactory {
    public ApiErrorResponse create(HttpStatus status, String message, HttpServletRequest request) {
        return create(status, message, request, null);
    }

    public ApiErrorResponse create(HttpStatus status, String message, HttpServletRequest request,
                                   Map<String, String> validationErrors) {
        return new ApiErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                message, request.getRequestURI(), validationErrors);
    }
}

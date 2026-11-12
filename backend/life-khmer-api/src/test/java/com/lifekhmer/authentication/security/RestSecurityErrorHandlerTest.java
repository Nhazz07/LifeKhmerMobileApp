package com.lifekhmer.authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifekhmer.common.response.ApiErrorResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class RestSecurityErrorHandlerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final ApiErrorResponseFactory factory = new ApiErrorResponseFactory();

    @Test void authenticationFailureUsesCommonJsonSchema() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/accounts/me");
        MockHttpServletResponse response = new MockHttpServletResponse();
        new RestAuthenticationEntryPoint(objectMapper, factory).commence(request, response, new BadCredentialsException("secret"));
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("\"error\":\"Unauthorized\""));
        assertFalse(response.getContentAsString().contains("secret"));
    }

    @Test void authorizationFailureUsesCommonJsonSchema() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/health/measurements");
        MockHttpServletResponse response = new MockHttpServletResponse();
        new RestAccessDeniedHandler(objectMapper, factory).handle(request, response, new AccessDeniedException("private"));
        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("\"error\":\"Forbidden\""));
        assertFalse(response.getContentAsString().contains("private"));
    }
}

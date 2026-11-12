package com.lifekhmer.authentication.security;

import com.lifekhmer.accounts.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Test
    void createsParsesAndValidatesTypedTokens() {
        JwtService service = serviceWithSecret(validSecret(), 60_000L, 120_000L);
        User user = user();

        String access = service.generateAccessToken(user);
        String refresh = service.generateRefreshToken(user);

        assertTrue(service.isTokenValid(access, "access"));
        assertTrue(service.isTokenValid(refresh, "refresh"));
        assertFalse(service.isTokenValid(access, "refresh"));
        assertEquals(user.getId(), service.extractUserId(access));
    }

    @Test
    void rejectsExpiredAndMalformedTokens() {
        JwtService service = serviceWithSecret(validSecret(), -1L, 60_000L);
        assertFalse(service.isTokenValid(service.generateAccessToken(user()), "access"));
        assertFalse(service.isTokenValid("not-a-jwt", "access"));
    }

    @Test
    void rejectsMissingInvalidAndShortSecrets() {
        assertConfigurationFailure(null, "JWT_SECRET must be configured");
        assertConfigurationFailure("not-a-standard-base64-secret-", "JWT_SECRET must be a valid");
        assertConfigurationFailure(Base64.getEncoder().encodeToString(new byte[31]), "at least 32 bytes");
    }

    private void assertConfigurationFailure(String secret, String message) {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> serviceWithSecret(secret, 60_000L, 60_000L));
        assertTrue(exception.getMessage().contains(message));
    }

    private JwtService serviceWithSecret(String secret, long accessExpiry, long refreshExpiry) {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secret", secret);
        ReflectionTestUtils.setField(service, "accessTokenExpiration", accessExpiry);
        ReflectionTestUtils.setField(service, "refreshTokenExpiration", refreshExpiry);
        ReflectionTestUtils.invokeMethod(service, "initializeSigningKey");
        return service;
    }

    private String validSecret() {
        return Base64.getEncoder().encodeToString(new byte[32]);
    }

    private User user() {
        User user = new User();
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        return user;
    }
}

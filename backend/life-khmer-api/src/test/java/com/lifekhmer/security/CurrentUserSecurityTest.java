package com.lifekhmer.security;

import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.authentication.security.JwtAuthenticationFilter;
import com.lifekhmer.authentication.security.JwtService;
import com.lifekhmer.common.exception.ForbiddenException;
import com.lifekhmer.common.exception.UnauthorizedException;
import com.lifekhmer.security.ownership.ResourceOwnershipService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrentUserSecurityTest {
    private final CurrentUserService currentUserService = new CurrentUserService();

    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void obtainsTypedPrincipalIdentity() {
        UUID id = UUID.randomUUID();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new CurrentUser(id), null, List.of()));
        assertTrue(currentUserService.isAuthenticated());
        assertEquals(id, currentUserService.getCurrentUserId());
        assertEquals(new CurrentUser(id), currentUserService.getCurrentUser());
    }

    @Test void unauthenticatedContextIsRejected() {
        assertFalse(currentUserService.isAuthenticated());
        assertThrows(UnauthorizedException.class, currentUserService::getCurrentUserId);
    }

    @Test void ownershipAllowsOnlyTheAuthenticatedUser() {
        UUID owner = UUID.randomUUID(); UUID other = UUID.randomUUID();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new CurrentUser(owner), null, List.of()));
        ResourceOwnershipService ownership = new ResourceOwnershipService(currentUserService);
        assertTrue(ownership.isOwner(owner));
        assertDoesNotThrow(() -> ownership.verifyOwnership(owner));
        assertFalse(ownership.isOwner(other));
        assertThrows(ForbiddenException.class, () -> ownership.verifyOwnership(other));
    }

    @Test void validAccessTokenCreatesCurrentUserPrincipal() throws Exception {
        JwtService jwt = mock(JwtService.class); UserRepository users = mock(UserRepository.class);
        UUID id = UUID.randomUUID();
        when(jwt.isTokenValid("access-token", "access")).thenReturn(true); when(jwt.extractUserId("access-token")).thenReturn(id); when(users.existsById(id)).thenReturn(true);
        new JwtAuthenticationFilter(jwt, users).doFilter(request("access-token"), new MockHttpServletResponse(), new MockFilterChain());
        assertEquals(new CurrentUser(id), SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test void invalidAndRefreshTokensNeverCreateAuthentication() throws Exception {
        JwtService jwt = mock(JwtService.class); UserRepository users = mock(UserRepository.class);
        when(jwt.isTokenValid("bad-token", "access")).thenReturn(false); when(jwt.isTokenValid("refresh-token", "access")).thenReturn(false);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwt, users);
        filter.doFilter(request("bad-token"), new MockHttpServletResponse(), new MockFilterChain());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        filter.doFilter(request("refresh-token"), new MockHttpServletResponse(), new MockFilterChain());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private MockHttpServletRequest request(String token) { MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/accounts/me"); request.addHeader("Authorization", "Bearer " + token); return request; }
}

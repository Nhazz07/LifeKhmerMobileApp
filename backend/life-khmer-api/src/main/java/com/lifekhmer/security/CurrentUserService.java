package com.lifekhmer.security;

import com.lifekhmer.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CurrentUserService {
    public UUID getCurrentUserId() { return getCurrentUser().id(); }
    public CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CurrentUser currentUser)) {
            throw new UnauthorizedException("Authentication is required");
        }
        return currentUser;
    }
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CurrentUser;
    }
}

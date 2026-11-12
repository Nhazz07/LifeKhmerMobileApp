package com.lifekhmer.security.ownership;

import com.lifekhmer.common.exception.ForbiddenException;
import com.lifekhmer.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

/** Personal-resource policy; explicit family-sharing can extend this later. */
@Service @RequiredArgsConstructor
public class ResourceOwnershipService {
    private final CurrentUserService currentUserService;
    public boolean isOwner(UUID resourceOwnerId) { return resourceOwnerId != null && resourceOwnerId.equals(currentUserService.getCurrentUserId()); }
    public void verifyOwnership(UUID resourceOwnerId) { if (!isOwner(resourceOwnerId)) throw new ForbiddenException("You do not have permission to access this resource"); }
}

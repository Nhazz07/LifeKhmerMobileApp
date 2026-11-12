# Current user and ownership

`JwtAuthenticationFilter` validates only access tokens, verifies that the token subject still exists in `users`, and stores `CurrentUser(UUID id)` as the authenticated Spring Security principal.

Services obtain the caller through injected `CurrentUserService`; never accept an owner ID from a request body, path, or query parameter for private resources.

For private records, prefer an owner-scoped repository query:

```java
repository.findByIdAndUserId(resourceId, currentUserService.getCurrentUserId());
```

This prevents an existence leak for another user's record and should normally map to `ResourceNotFoundException`. When a service already has the resource owner ID, use `ResourceOwnershipService.verifyOwnership(ownerId)`, which throws `ForbiddenException` on a mismatch.

Future family sharing must be an explicit, audited permission check composed with this policy. Family membership alone must not replace owner-scoped queries or grant blanket access.

package com.lifekhmer.authentication.entity;

import com.lifekhmer.accounts.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter @NoArgsConstructor
public class RefreshToken {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "revoked_at") private LocalDateTime revokedAt;
    @Column(name = "replaced_by_token_id") private UUID replacedByTokenId;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void created() { if (id == null) id = UUID.randomUUID(); if (createdAt == null) createdAt = LocalDateTime.now(); }
    public boolean isActive() { return revokedAt == null && expiresAt.isAfter(LocalDateTime.now()); }
}

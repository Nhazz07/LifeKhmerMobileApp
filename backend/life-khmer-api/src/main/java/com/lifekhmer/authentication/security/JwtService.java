package com.lifekhmer.authentication.security;

import com.lifekhmer.accounts.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    private static final int MINIMUM_HS256_KEY_BYTES = 32;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey signingKey;

    @PostConstruct
    void initializeSigningKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be configured as a standard Base64 value");
        }
        final byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret.trim());
        } catch (RuntimeException exception) {
            throw new IllegalStateException("JWT_SECRET must be a valid standard Base64-encoded value", exception);
        }
        if (keyBytes.length < MINIMUM_HS256_KEY_BYTES) {
            throw new IllegalStateException("JWT_SECRET must decode to at least 32 bytes for HS256 signing");
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        return generateToken(user, accessTokenExpiration, "access");
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, "refresh");
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public boolean isTokenValid(String token, String expectedType) {
        try {
            Claims claims = extractAllClaims(token);
            return expectedType.equals(claims.get("type", String.class))
                    && claims.getExpiration().after(new Date());
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    private String generateToken(User user, long expiration, String tokenType) {
        Date issuedAt = new Date();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", tokenType)
                .issuedAt(issuedAt)
                .expiration(new Date(issuedAt.getTime() + expiration))
                .signWith(signingKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }
}

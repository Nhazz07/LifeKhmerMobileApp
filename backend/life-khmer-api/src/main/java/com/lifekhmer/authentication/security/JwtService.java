package com.lifekhmer.authentication.security;

import com.lifekhmer.accounts.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-tokem-expiration}")
    private Long refreshTokenExpiration;


    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes)
    }
    
    // Generate access token
    public String generateAccessToken(User user){
        return  generateToken(user, accessTokenExpiration, "access");
    }

    // Generate refresh token
    public String generateRefreshToken(User user){
        return generateToken(user, refreshTokenExpiration, "refresh");
    }

    // share token generation logic
    private String generateToken(User user, long expiration, String tokenType){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", tokenType)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    // Extract user Id
    public UUID extractUserId(String token){
        String subject = extractClaim(token, Claims::getSubject);
        return UUID.fromString(subject);
    }
    // Extract a specific claim
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate token for a specific response
    public boolean isTokenValid(String token, String expectedType){
        try {
            Claims claims = extractAllClaims(token);

            String actualType = claims.get("type", String.class);

            return expectedType.equals(actualType) && claims.getExpiration().after(new Date());
        }catch(Exception exception){
            return false;
        }
    }

    public long getAccessTokenExpiration(){
        return accessTokenExpiration;
    }
}

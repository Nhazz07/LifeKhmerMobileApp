package com.lifekhmer.authentication.service.impl;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.accounts.repository.UserRepository;
import com.lifekhmer.authentication.dto.request.LoginRequest;
import com.lifekhmer.authentication.dto.request.RegisterRequest;
import com.lifekhmer.authentication.dto.response.LoginResponse;
import com.lifekhmer.authentication.dto.response.RegisterResponse;
import com.lifekhmer.authentication.entity.Account;
import com.lifekhmer.authentication.entity.eum.AuthProvider;
import com.lifekhmer.authentication.mapper.AuthMapper;
import com.lifekhmer.authentication.repository.AccountRepository;
import com.lifekhmer.authentication.repository.RefreshTokenRepository;
import com.lifekhmer.authentication.entity.RefreshToken;
import com.lifekhmer.authentication.dto.request.RefreshTokenRequest;
import com.lifekhmer.common.exception.BadRequestException;
import com.lifekhmer.common.exception.DuplicateResourceException;
import com.lifekhmer.common.exception.UnauthorizedException;
import com.lifekhmer.authentication.security.JwtService;
import com.lifekhmer.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        // check whether the email is already exist
        if(accountRepository.existsByProviderAndProviderSubject(AuthProvider.LOCAL, email)){
            throw new DuplicateResourceException("An account with this email already exists");
        }

        // convert registration request into User entity
        User user = authMapper.toUserEntity(request);

        // save user
        userRepository.save(user);

        // create account
        Account account = new Account();
        account.setUser(user);
        account.setProvider(AuthProvider.LOCAL);
        account.setProviderSubject(email);
        account.setPasswordHash(passwordEncoder.encode(request.password()));

        // save account
        accountRepository.save(account);

        // return registration response
        return authMapper.toRegisterResponse(user, account);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        // Normalize email
        String email = request.email().trim().toLowerCase();

        Account account = accountRepository.findByProviderAndProviderSubject(AuthProvider.LOCAL,
                email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if(!passwordEncoder.matches(
                request.password(),
                account.getPasswordHash()
        )){
            throw new UnauthorizedException("Invalid email or password");
        }

        // retrieve associated user
        User user = account.getUser();

        // Generate token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = issueRefreshToken(user);

        // return response
        return authMapper.toLoginResponse(
                user,
                account,
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpiration()
        );
    }

    @Override @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        if (!jwtService.isTokenValid(request.refreshToken(), "refresh")) throw new UnauthorizedException("Invalid or expired refresh token");
        RefreshToken old = refreshTokenRepository.findByTokenHash(hash(request.refreshToken())).orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
        if (!old.isActive()) throw new UnauthorizedException("Refresh token has been revoked or expired");
        old.setRevokedAt(LocalDateTime.now());
        String raw = issueRefreshToken(old.getUser());
        RefreshToken replacement = refreshTokenRepository.findByTokenHash(hash(raw)).orElseThrow();
        old.setReplacedByTokenId(replacement.getId());
        Account account = accountRepository.findByUserIdAndProvider(old.getUser().getId(), AuthProvider.LOCAL).orElseThrow(() -> new BadRequestException("No local account is linked"));
        return authMapper.toLoginResponse(old.getUser(), account, jwtService.generateAccessToken(old.getUser()), raw, jwtService.getAccessTokenExpiration());
    }

    @Override @Transactional
    public void logout(String rawToken) {
        refreshTokenRepository.findByTokenHash(hash(rawToken)).ifPresent(token -> { if (token.getRevokedAt() == null) token.setRevokedAt(LocalDateTime.now()); });
    }
    private String issueRefreshToken(User user) {
        String raw = jwtService.generateRefreshToken(user);
        RefreshToken stored = new RefreshToken(); stored.setUser(user); stored.setTokenHash(hash(raw)); stored.setExpiresAt(LocalDateTime.now().plusSeconds(jwtService.getRefreshTokenExpiration() / 1000)); refreshTokenRepository.save(stored); return raw;
    }
    private String hash(String value) { try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); } catch (Exception e) { throw new IllegalStateException("Cannot hash token", e); } }
}

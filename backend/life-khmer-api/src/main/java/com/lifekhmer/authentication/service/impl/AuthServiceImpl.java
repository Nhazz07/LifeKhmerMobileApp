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
import com.lifekhmer.authentication.security.JwtService;
import com.lifekhmer.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        // check whether the email is already exist
        if(accountRepository.existsByProviderAndProviderSubject(AuthProvider.LOCAL, email)){
            throw new IllegalArgumentException("Email is already exist");
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if(!passwordEncoder.matches(
                request.password(),
                account.getPasswordHash()
        )){
            throw new IllegalArgumentException("Invalid email or password");
        }

        // retrieve associated user
        User user = account.getUser();

        // Generate token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // return response
        return authMapper.toLoginResponse(
                user,
                account,
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpiration()
        );
    }
}

package com.lifekhmer.authentication.mapper;

import com.lifekhmer.accounts.entity.User;
import com.lifekhmer.authentication.dto.request.RegisterRequest;
import com.lifekhmer.authentication.dto.response.LoginResponse;
import com.lifekhmer.authentication.dto.response.RegisterResponse;
import com.lifekhmer.authentication.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toUserEntity(RegisterRequest request){
        User user = new User();

        user.setFullName(request.fullName());
        user.setDateOfBirth(request.dateOfBirth());

        return user;
    }

    public RegisterResponse toRegisterResponse(User user, Account account){
        return new RegisterResponse(
                user.getId(),
                account.getId(),
                user.getFullName(),
                account.getProviderSubject(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                "Registration successful"
        );
    }

    public LoginResponse toLoginResponse(
            User user,
            Account account,
            String accessToken,
            String refreshToken,
            long expiresIn
    ) {
        return new LoginResponse(
                user.getId(),
                account.getId(),
                user.getFullName(),
                account.getProviderSubject(),
                accessToken,
                refreshToken,
                "Bearer",
                expiresIn,
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}

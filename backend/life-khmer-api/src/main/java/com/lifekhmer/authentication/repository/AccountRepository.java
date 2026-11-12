package com.lifekhmer.authentication.repository;

import com.lifekhmer.authentication.entity.Account;
import com.lifekhmer.authentication.entity.eum.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>{
    Optional<Account> findByProviderAndProviderSubject(
            AuthProvider provider,
            String providerSubject
    );

    boolean existsByProviderAndProviderSubject(
            AuthProvider provider,
            String providerSubject
    );

    Optional<Account> findByUserIdAndProvider(UUID userId, AuthProvider provider);
}

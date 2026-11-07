package com.lifekhmer.accounts.repository;

import com.lifekhmer.accounts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

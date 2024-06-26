package com.example.rewardservice.repository;

import com.example.rewardservice.domain.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUserId(UUID userId);
    Optional<User> findByUserId(String userId);

}

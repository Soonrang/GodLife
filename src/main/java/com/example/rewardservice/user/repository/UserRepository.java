package com.example.rewardservice.user.repository;

import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailId(String email);
    //<User> findByUserEmailAndUserSocial(String email, boolean social);
    boolean existsByEmailId(String email);

}

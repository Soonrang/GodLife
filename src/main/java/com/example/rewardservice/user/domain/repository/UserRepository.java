package com.example.rewardservice.user.domain.repository;

import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    //<User> findByUserEmailAndUserSocial(String email, boolean social);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);


}

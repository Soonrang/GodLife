package com.example.rewardservice.user.repository;

import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

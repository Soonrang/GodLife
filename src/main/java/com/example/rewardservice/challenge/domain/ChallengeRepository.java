package com.example.rewardservice.challenge.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    Page<Challenge> findByCategory(String category, Pageable pageable);
    Page<Challenge> findByCategoryAndStatus(String category, String status, Pageable pageable);

}

package com.example.rewardservice.challenge.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
}

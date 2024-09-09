package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.PostChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengePostRepository extends JpaRepository<PostChallenge, UUID> {
}

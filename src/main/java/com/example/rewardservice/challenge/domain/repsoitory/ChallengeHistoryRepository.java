package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.ChallengeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeHistoryRepository extends JpaRepository<ChallengeHistory, UUID> {
}

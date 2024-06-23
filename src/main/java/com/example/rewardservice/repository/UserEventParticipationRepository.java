package com.example.rewardservice.repository;

import com.example.rewardservice.domain.User.UserEventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventParticipationRepository extends JpaRepository<UserEventParticipation, UUID> {
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);
}

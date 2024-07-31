package com.example.rewardservice.event.domain.repository;

import com.example.rewardservice.event.domain.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, UUID> {
}

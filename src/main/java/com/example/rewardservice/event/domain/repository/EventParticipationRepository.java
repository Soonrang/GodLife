package com.example.rewardservice.event.domain.repository;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, UUID> {
    List<EventParticipation> findByUserAndEventAndCreatedAtBetween(User user, Event event, LocalDateTime start, LocalDateTime end);
}

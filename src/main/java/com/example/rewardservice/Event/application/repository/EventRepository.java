package com.example.rewardservice.Event.application.repository;

import com.example.rewardservice.Event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findById(UUID eventId);
}

package com.example.rewardservice.event.application.repository;

import com.example.rewardservice.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findById(UUID eventId);
}

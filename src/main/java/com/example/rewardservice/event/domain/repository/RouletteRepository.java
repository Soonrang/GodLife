package com.example.rewardservice.event.domain.repository;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.Events.RouletteEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RouletteRepository extends JpaRepository<RouletteEvent, UUID> {
   // Optional<Event> findById(UUID eventId);
}

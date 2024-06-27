package com.example.rewardservice.repository;

import com.example.rewardservice.domain.Event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByCreatedAtAsc();
    List<Event> findAllByCreatedAtDesc();
}

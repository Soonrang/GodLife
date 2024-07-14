package com.example.rewardservice.Participation;

import com.example.rewardservice.Event.domain.Event;
import com.example.rewardservice.Participation.domain.Participation;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {

    //List<Participation> findByUserAndParticipationDateBetween(User user, Event event, LocalDateTime startTime, LocalDateTime endTime);
    List<Participation> findByUserAndParticipationDateBetween(User user, LocalDateTime start, LocalDateTime end);

}

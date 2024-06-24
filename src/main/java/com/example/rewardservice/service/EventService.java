package com.example.rewardservice.service;

import com.example.rewardservice.domain.Point.Point;
import com.example.rewardservice.domain.User.UserEventParticipation;
import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.repository.EventRepository;
import com.example.rewardservice.repository.PointRepository;
import com.example.rewardservice.repository.UserEventParticipationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserEventParticipationRepository participationRepository;
    private final PointRepository pointRepository;
    private final PointService pointService;

    @Transactional
    public PointLogDTO participateInEvent(String userId, UUID eventId, String answer) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        Point point = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Check participation logic here (daily/once per event, etc.)
        if (participationRepository.existsByUserIdAndEventId(point.getId(), eventId)) {
            throw new IllegalArgumentException("User already participated in this event.");
        }

        // Verify the answer, for quiz event example:
        if ("퀴즈 이벤트".equals(event.getName()) && !"correctAnswer".equals(answer)) { // Replace with actual answer checking logic
            throw new IllegalArgumentException("Incorrect answer.");
        }

        UserEventParticipation participation = UserEventParticipation.builder()
                .user(point)
                .event(event)
                .participatedAt(LocalDateTime.now())
                .build();
        participationRepository.save(participation);

        if ("출석 이벤트".equals(event.getName())) {
            return pointService.earnPoints(point.getId(), 100, "출석 이벤트 참여");
        } else if ("룰렛 이벤트".equals(event.getName())) {
            return pointService.earnPoints(point.getId(), 50, "룰렛 이벤트 참여");
        } else if ("퀴즈 이벤트".equals(event.getName())) {
            return pointService.earnPoints(point.getId(), 200, "퀴즈 이벤트 참여");
        }
        throw new IllegalArgumentException("Unknown event type");
    }
}
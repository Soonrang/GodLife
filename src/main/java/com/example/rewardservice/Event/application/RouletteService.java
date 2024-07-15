package com.example.rewardservice.Event.application;

import com.example.rewardservice.Event.application.repository.EventRepository;
import com.example.rewardservice.Event.domain.Event;
import com.example.rewardservice.Point.EarnedPointRepository;
import com.example.rewardservice.Point.domain.EarnedPoint;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final UserRepository userRepository;
    private final EarnedPointRepository earnedPointRepository;
    private final EventRepository eventRepository;

    private static final int MAX_DAILY_SPINS = 3;
    private static final String ATTENDANCE_MESSAGE = "룰렛";

    public void participateInRouletteEvent(String userEmail,UUID eventId, long earnedPoints){
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        if (hasParticipatedToday(userEmail, eventId)) {
            throw new RuntimeException("오늘 룰렛 참여 횟수를 초과했습니다.");
        }

        user.earnPoints(earnedPoints);
        userRepository.save(user);

        EarnedPoint earnedPoint = EarnedPoint.builder()
                .event(event)
                .user(user)
                .participationCount(DailyRouletteCount(eventId, userEmail) + 1)
                .isWinner(false)
                .pointChange(earnedPoints)
                .description(ATTENDANCE_MESSAGE)
                .build();

        earnedPointRepository.save(earnedPoint);
    }

    private boolean hasParticipatedToday(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EarnedPoint> earnedPoints = earnedPointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return earnedPoints.size() >= MAX_DAILY_SPINS;
    }

    private int DailyRouletteCount(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EarnedPoint> points = earnedPointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfDay, endOfDay);

        return points.size();
    }

    private Event findEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + userEmail));
    }

}

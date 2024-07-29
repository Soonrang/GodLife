package com.example.rewardservice.event.application.service;

import com.example.rewardservice.event.domain.Events.RouletteEvent;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.repository.RouletteRepository;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.domain.EarnedPoint;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
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
    private final PointRepository pointRepository;
    private final EventRepository eventRepository;
    private final RouletteRepository rouletteRepository;
    private final PointService pointService;

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

        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(userEmail)
                .eventId(eventId)
                .pointChange(earnedPoints)
                .description(ATTENDANCE_MESSAGE)
                .isWinner(false)
                .participationCount(DailyRouletteCount(eventId, userEmail) + 1)
                .build();

        pointService.addEarnedPoint(addPointRequest);

        RouletteEvent rouletteEvent = new RouletteEvent(event, earnedPoints);
        rouletteRepository.save(rouletteEvent);
    }

    public boolean hasParticipatedToday(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EarnedPoint> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return earnedPoints.size() >= MAX_DAILY_SPINS;
    }

    public int getRouletteCount(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);

        //하루 조회
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EarnedPoint> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        //돌릴 수 있는 룰렛 카운트
        return MAX_DAILY_SPINS - earnedPoints.size();
    }

    private int DailyRouletteCount(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
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

package com.example.rewardservice.event.application.service;

import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.AddPointRequest;
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
    private final PointService pointService;
    private final EventParticipationRepository eventParticipationRepository;

    private static final int MAX_DAILY_SPINS = 3;
    private static final String ROULETTE_MESSAGE = "룰렛";

    public void participateInRouletteEvent(String userEmail, UUID eventId, long earnedPoints){
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        if (hasParticipatedToday(userEmail, eventId)) {
            throw new RuntimeException("오늘 룰렛 참여 횟수를 초과했습니다.");
        }

        user.earnPoints(earnedPoints);
        userRepository.save(user);

        //포인트 기록 전달
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(userEmail)
                .eventId(eventId)
                .point(earnedPoints)
                .description(ROULETTE_MESSAGE)
                .build();

        //이벤트 참여 리스트 생성
        EventParticipation participation = EventParticipation.builder()
                .user(user)
                .event(event)
                .pointEarned(addPointRequest.getPoint())
                .description(addPointRequest.getDescription())
                .build();

        pointService.addEarnedPoint(addPointRequest);
        eventParticipationRepository.save(participation);
        eventRepository.save(event);
    }

    public boolean hasParticipatedToday(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return earnedPoints.size() >= MAX_DAILY_SPINS;
    }

    public int getRouletteCount(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return MAX_DAILY_SPINS - earnedPoints.size();
    }

    private int DailyRouletteCount(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
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
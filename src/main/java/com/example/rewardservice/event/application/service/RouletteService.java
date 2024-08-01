package com.example.rewardservice.event.application.service;

import com.example.rewardservice.common.ValidateService;
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
    private final ValidateService validateService;

    private static final int MAX_DAILY_SPINS = 3;
    private static final String ROULETTE_MESSAGE = "룰렛";

    public void participateInRouletteEvent(String email, UUID eventId, long earnedPoints){
        Event event = validateService.findByEventId(eventId);
        User user = validateService.findByUserEmail(email);

        if (hasParticipatedToday(email, eventId)) {
            throw new RuntimeException("오늘 룰렛 참여 횟수를 초과했습니다.");
        }

        user.earnPoints(earnedPoints);
        userRepository.save(user);

        // 이벤트 참여 기록 저장
        EventParticipation participation = EventParticipation.builder()
                .user(user)
                .event(event)
                .points(earnedPoints)
                .build();

        eventParticipationRepository.save(participation);

        // 포인트 적립 기록 저장
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .eventId(eventId)
                .point(earnedPoints)
                .activityId(participation.getId())
                .build();

        eventParticipationRepository.save(participation);
        pointService.addEarnedPoint(addPointRequest);
        eventRepository.save(event);
    }

    public boolean hasParticipatedToday(String userEmail, UUID eventId) {
        User user = validateService.findByUserEmail(userEmail);
        Event event = validateService.findByEventId(eventId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return earnedPoints.size() >= MAX_DAILY_SPINS;
    }

    public int getRouletteCount(String userEmail, UUID eventId) {
        User user = validateService.findByUserEmail(userEmail);
        Event event = validateService.findByEventId(eventId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> earnedPoints = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return MAX_DAILY_SPINS - earnedPoints.size();
    }

    private int DailyRouletteCount(UUID eventId, String userEmail) {
        Event event = validateService.findByEventId(eventId);
        User user = validateService.findByUserEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<Point> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfDay, endOfDay);

        return points.size();
    }

}
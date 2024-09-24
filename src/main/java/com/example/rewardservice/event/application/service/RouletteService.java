package com.example.rewardservice.event.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.event.application.request.AddPointRequest;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.domain.Point;
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
    private final EventParticipationRepository eventParticipationRepository;
    private final ValidateService validateService;
    private final PointService pointService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private static final int MAX_DAILY_SPINS = 3;
    private static final String ROULETTE_MESSAGE = "룰렛";

    public void participateInRouletteEvent(String email, UUID eventId, long earnedPoints){
        Event event = findByEventId(eventId);
        User user = findByUserEmail(email);

        validateCount(email, eventId);

        // 이벤트 참여 기록 저장
        EventParticipation participation = new EventParticipation(user, event, earnedPoints, ROULETTE_MESSAGE);
        eventParticipationRepository.save(participation);

        // 포인트 적립 기록 저장
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .points(earnedPoints)
                .description(LocalDate.now().getMonthValue()+"월 룰렛이벤트 참여")
                .activityId(participation.getId())
                .build();
        pointService.addEarnedPoint(addPointRequest);
    }

    public boolean hasParticipatedToday(String userEmail, UUID eventId) {
        User user = validateService.findByUserEmail(userEmail);
        Event event = validateService.findByEventId(eventId);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EventParticipation> participations = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return participations.size() >= MAX_DAILY_SPINS;
    }

    public int getRouletteCount(String userEmail, UUID eventId) {
        User user = validateService.findByUserEmail(userEmail);
        Event event = validateService.findByEventId(eventId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EventParticipation> participations = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return MAX_DAILY_SPINS - participations.size();
    }

    private int DailyRouletteCount(UUID eventId, String userEmail) {
        Event event = validateService.findByEventId(eventId);
        User user = validateService.findByUserEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();

        List<EventParticipation> points = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfDay, endOfDay);

        return points.size();
    }
    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }
    public Event findByEventId(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }


    private void validateCount(String email, UUID eventId){
        if (hasParticipatedToday(email, eventId)) {
            throw new RuntimeException("오늘 룰렛 참여 횟수를 초과했습니다.");
        }
    }

}
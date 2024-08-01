package com.example.rewardservice.event.application.service;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.application.dto.ViewPointRequest;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ViewCountService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PointService pointService;
    private final EventParticipationRepository eventParticipationRepository;
    private static final String EVENT_VIEW_DESCRIPTION_MESSAGE = "이벤트 입니다.";
    @Transactional
    public void viewPoints(UUID eventId, String email, long earnedPoints) {
        User user = findUserByEmail(email);
        Event event = findEventById(eventId);

        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .eventId(eventId)
                .point(earnedPoints)
                .description(event.getName()+EVENT_VIEW_DESCRIPTION_MESSAGE)
                .build();

        EventParticipation participation = EventParticipation.builder()
                .user(user)
                .event(event)
                .points(addPointRequest.getPoint())
                .description(addPointRequest.getDescription())
                .build();

        pointService.addEarnedPoint(addPointRequest);
        eventParticipationRepository.save(participation);
        eventRepository.save(event);
    }


    public boolean checkIfUserParticipatedToday(UUID eventId, String userEmail) {
        User user = findByUserEmail(userEmail);
        Event event = findEventById(eventId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<EventParticipation> participations = eventParticipationRepository
                .findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return !participations.isEmpty();
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Event findEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

}

package com.example.rewardservice.event.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.event.application.request.AddPointRequest;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.point.application.PointService;
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
    private final UserRepository userRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final ValidateService validateService;
    private final PointService pointService;

    private static final String EVENT_VIEW_DESCRIPTION_MESSAGE = "페이지 열람 보상";
    @Transactional
    public void viewPoints(UUID eventId, String email, long earnedPoints) {
        User user = validateService.findByUserEmail(email);
        Event event = validateService.findByEventId(eventId);

        EventParticipation participation = new EventParticipation(user, event, earnedPoints, EVENT_VIEW_DESCRIPTION_MESSAGE);

        // 포인트 적립 기록 저장
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .points(earnedPoints)
                .description("이벤트 참여: " + event.getName())
                .activityId(participation.getId())
                .build();
        pointService.addEarnedPoint(addPointRequest);
    }


    public boolean checkIfUserParticipatedToday(UUID eventId, String email) {
        User user = validateService.findByUserEmail(email);
        Event event = validateService.findByEventId(eventId);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<EventParticipation> participations = eventParticipationRepository
                .findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return !participations.isEmpty(); //참여 기록이 있으면 true
    }
}

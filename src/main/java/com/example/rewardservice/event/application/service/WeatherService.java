package com.example.rewardservice.event.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.event.application.request.AddPointRequest;
import com.example.rewardservice.event.application.request.WeatherPointRequest;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.domain.PointRepository;
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
public class WeatherService {

    private final EventParticipationRepository eventParticipationRepository;
    private final ValidateService validateService;
    private final PointService pointService;


    public void getWeatherPoints(UUID eventId,String userEmail,WeatherPointRequest weatherPointRequest) {
        User user = validateService.findByUserEmail(userEmail);
        Event event = validateService.findByEventId(eventId);

        if(hasParticipatedToday(user,event)) {
            throw new RuntimeException("오늘은 이미 출석을 했습니다.");
        }

        long points = weatherPointRequest.getPoints();
        EventParticipation participation = new EventParticipation(user, event, points, "날씨 이벤트 참여");
        eventParticipationRepository.save(participation);

        AddPointRequest addPointRequest = new AddPointRequest(userEmail, points, "날씨 이벤트 참여", participation.getId());
        pointService.addEarnedPoint(addPointRequest);
    }

    private boolean hasParticipatedToday(User user, Event event) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();
        List<EventParticipation> participations = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return !participations.isEmpty();
    }
}

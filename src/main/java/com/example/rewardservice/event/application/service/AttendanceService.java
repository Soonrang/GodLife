package com.example.rewardservice.event.application.service;

import com.example.rewardservice.event.application.dto.response.MonthlyAttendanceResponse;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final PointService pointService;
    private final EventParticipationRepository eventParticipationRepository;

    private static final long ATTENDANCE_POINT = 100;
    private static final long BONUS_POINT = 100;
    private static final int BONUS_PERIOD = 10;
    private static final String EVENT_DESCRIPTION_MESSAGE = "출석";
    private static final String EVENT_DESCRIPTION_BONUS_MESSAGE = "출석 보너스";

    @Transactional
    public void participateInAttendanceEvent(UUID eventId, String email) {
        User user = findUserByEmail(email);
        Event event = findEventById(eventId);

        if (checkTodayAttendance(event, user)) {
            throw new RuntimeException("오늘 이미 출석하셨습니다.");
        }

        int monthlyCount = MonthlyAttendanceCount(event, user) + 1;
        long pointsToEarn = ATTENDANCE_POINT;
        String description = EVENT_DESCRIPTION_MESSAGE;

        if(monthlyCount % BONUS_PERIOD == 0){
            pointsToEarn += BONUS_POINT;
            description = EVENT_DESCRIPTION_BONUS_MESSAGE;
        }

        // 이벤트 참여 기록 저장
        EventParticipation participation = EventParticipation.builder()
                .user(user)
                .event(event)
                .points(pointsToEarn)
                .description(description)
                .build();

        eventParticipationRepository.save(participation);

        // 포인트 적립 기록 저장
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .eventId(eventId)
                .point(pointsToEarn)
                .description(description)
                .activityId(participation.getId())
                .build();

        eventParticipationRepository.save(participation);
        pointService.addEarnedPoint(addPointRequest);
        eventRepository.save(event);
    }

    public MonthlyAttendanceResponse getAttendanceData(String email, UUID eventId) {
        User user = findUserByEmail(email);
        Event event = findEventById(eventId);

        LocalDateTime startOfMonth = event.getEventPeriod().getStartDate();
        LocalDateTime endOfMonth = event.getEventPeriod().getEndDate();

        List<EventParticipation> points = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

        int attendanceCount = points.size();
        long totalPoints = points.stream().mapToLong(EventParticipation::getPoints).sum();
        boolean hasAttendance = checkTodayAttendance(event, user);

        return new MonthlyAttendanceResponse(attendanceCount, totalPoints, hasAttendance);
    }

    private boolean checkTodayAttendance(Event event, User user) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<Point> points = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return !points.isEmpty();
    }

    private int MonthlyAttendanceCount(Event event, User user) {
        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<Point> points = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfMonth, endOfMonth);

        return points.size();
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
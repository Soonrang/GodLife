package com.example.rewardservice.event.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.event.application.request.AddPointRequest;
import com.example.rewardservice.event.application.response.MonthlyAttendanceResponse;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.application.PointService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final UserRepository userRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final ValidateService validateService;
    private final PointService pointService;

    private static final long ATTENDANCE_POINT = 100;
    private static final long BONUS_POINT = 100;
    private static final int BONUS_PERIOD = 10;
    private static final String EVENT_DESCRIPTION_MESSAGE = "출석";
    private static final String EVENT_DESCRIPTION_BONUS_MESSAGE = "출석 보너스";

    @Transactional
    public void participateInAttendanceEvent(UUID eventId, String email) {
        User user = validateService.findByUserEmail(email);
        Event event = validateService.findByEventId(eventId);

        //검증
        if (checkTodayAttendance(event, user)) {
            throw new RuntimeException("이미출석.");
        }

        int monthlyCount = MonthlyAttendanceCount(event, user) + 1;
        long earnedPoint = ATTENDANCE_POINT;
        String description = EVENT_DESCRIPTION_MESSAGE;

        if(monthlyCount % BONUS_PERIOD == 0){
            earnedPoint += BONUS_POINT;
            description = EVENT_DESCRIPTION_BONUS_MESSAGE;
        }

        // 이벤트 참여 기록 저장
        EventParticipation participation = new EventParticipation(user, event, earnedPoint, description);
        eventParticipationRepository.save(participation);

        // 포인트 적립 기록 저장
        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(email)
                .points(earnedPoint)
                .description(LocalDate.now().getMonthValue()+"월 출석이벤트 참여")
                .activityId(participation.getId())
                .build();
        pointService.addEarnedPoint(addPointRequest);
    }

    public MonthlyAttendanceResponse getAttendanceData(String email, UUID eventId) {
        User user = validateService.findByUserEmail(email);
        Event event = validateService.findByEventId(eventId);

        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<EventParticipation> points = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

        int attendanceCount = points.size();
        long totalPoints = points.stream().mapToLong(EventParticipation::getPoints).sum();
        boolean hasAttendance = checkTodayAttendance(event, user);

        // 출석 날짜 리스트를 생성
        List<LocalDateTime> createdAt = points.stream()
                .map(EventParticipation::getCreatedAt)
                .collect(Collectors.toList());

        return new MonthlyAttendanceResponse(attendanceCount, totalPoints, hasAttendance, createdAt);
    }

    private boolean checkTodayAttendance(Event event, User user) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<EventParticipation> participations = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);
        return !participations.isEmpty();
    }

    private int MonthlyAttendanceCount(Event event, User user) {
        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<EventParticipation> participations = eventParticipationRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfMonth, endOfMonth);

        return participations.size();
    }
}

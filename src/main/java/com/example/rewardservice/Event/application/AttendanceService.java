package com.example.rewardservice.Event.application;

import com.example.rewardservice.Event.application.dto.response.MonthlyAttendanceResponse;
import com.example.rewardservice.Event.application.repository.EventRepository;
import com.example.rewardservice.Event.domain.Event;
import com.example.rewardservice.Point.EarnedPointRepository;
import com.example.rewardservice.Point.domain.EarnedPoint;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
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
    private final EarnedPointRepository earnedPointRepository;

    private static final long ATTENDANCE_POINT = 50;
    private static final long BONUS_POINT = 100;
    private static final int BONUS_PERIOD = 10;
    private static final List<Integer> BONUS_DAYS = List.of(10, 20, 30);
    private static final String ATTENDANCE_MESSAGE = "출석";
    private static final String BONUS_ATTENDANCE_MESSAGE = "출석 보너스";


    //출석 이벤트 참여
    @Transactional
    public void participateInAttendanceEvent(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        if (checkTodayAttendance(eventId, userEmail)) {
            throw new RuntimeException("오늘 이미 출석하셨습니다.");
        }

        int monthlyCount = MonthlyAttendanceCount(eventId, userEmail) + 1;
        LocalDateTime now = LocalDateTime.now();
        long pointsToEarn = ATTENDANCE_POINT;
        String description = ATTENDANCE_MESSAGE;

//        if (BONUS_DAYS.contains(now.getDayOfMonth())) {
//            pointsToEarn += BONUS_POINT;
//            description = BONUS_ATTENDANCE_MESSAGE;
//        }

        if(monthlyCount % BONUS_PERIOD == 0){
            pointsToEarn += BONUS_POINT;
            description = BONUS_ATTENDANCE_MESSAGE;
        }

        EarnedPoint participation = EarnedPoint.builder()
                .event(event)
                .user(user)
                .participationCount(monthlyCount)
                .isWinner(false)
                .pointChange(pointsToEarn)
                .description(description)
                .build();

        earnedPointRepository.save(participation);

        user.earnPoints(pointsToEarn);
        userRepository.save(user);
    }


    //이번달 출석횟수, 누적포인트, 마지막 출석일
    public MonthlyAttendanceResponse getAttendanceData(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);

        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<EarnedPoint> points = earnedPointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

        int attendanceCount = points.size();
        long totalPoints = points.stream().mapToLong(EarnedPoint::getPointChange).sum();
        String lastAttendanceDay = points.isEmpty() ? null : points.get(points.size() - 1).getCreatedAt().toLocalDate().toString();

        return new MonthlyAttendanceResponse(attendanceCount, totalPoints, lastAttendanceDay);
    }


    // 이번달 출석
    public int MonthlyAttendanceCount(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<EarnedPoint> points = earnedPointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

        return points.size();
    }


    public boolean checkTodayAttendance(UUID eventId, String userEmail) {
        Event event = findEventById(eventId);
        User user = findUserByEmail(userEmail);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<EarnedPoint> points = earnedPointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return !points.isEmpty();
    }

    //


    private Event findEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + userEmail));
    }


}

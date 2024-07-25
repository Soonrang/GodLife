package com.example.rewardservice.event.application;

import com.example.rewardservice.event.application.dto.response.MonthlyAttendanceResponse;
import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventType.AttendanceEvent;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.domain.EarnedPoint;
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
    private final PointRepository pointRepository;
    private final PointService pointService;

    private static final long ATTENDANCE_POINT = 100;
    private static final long BONUS_POINT = 100;
    private static final int BONUS_PERIOD = 10;
    private static final List<Integer> BONUS_DAYS = List.of(10, 20, 30);
    private static final String EVENT_DESCRIPTION_MESSAGE = "출석";
    private static final String EVENT_DESCRIPTION_BONUS_MESSAGE = "출석 보너스";

    // 출석 이벤트 참여
    @Transactional
    public void participateInAttendanceEvent(UUID eventId, String userEmail) {
        User user = findUserByEmail(userEmail);
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

        AddPointRequest addPointRequest = AddPointRequest.builder()
                .userEmail(userEmail)
                .eventId(eventId)
                .pointChange(pointsToEarn)
                .description(description)
                .isWinner(false)
                .participationCount(monthlyCount)
                .build();

        pointService.addEarnedPoint(addPointRequest);
    }

    // 이번달 출석횟수, 누적포인트, 마지막 출석일
    public MonthlyAttendanceResponse getAttendanceData(String userEmail, UUID eventId) {
        User user = findUserByEmail(userEmail);
        Event event = findEventById(eventId);

        LocalDateTime startOfMonth = event.getStartDate();
        LocalDateTime endOfMonth = event.getEndDate();

        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

        int attendanceCount = points.size();
        long totalPoints = points.stream().mapToLong(EarnedPoint::getPointChange).sum();
        boolean isChecked = checkTodayAttendance(event, user);

        return new MonthlyAttendanceResponse(attendanceCount, totalPoints, isChecked, (AttendanceEvent) event);
    }

    private boolean checkTodayAttendance(Event event, User user) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfDay, endOfDay);

        return !points.isEmpty();
    }

    private int MonthlyAttendanceCount(Event event, User user) {
        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);

        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
                user, event, startOfMonth, endOfMonth);

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

//    private final EventRepository eventRepository;
//    private final UserRepository userRepository;
//    private final PointRepository pointRepository;
//    private final PointService pointService;
//
//    private static final long ATTENDANCE_POINT = 100;
//    private static final long BONUS_POINT = 100;
//    private static final int BONUS_PERIOD = 10;
//    private static final List<Integer> BONUS_DAYS = List.of(10, 20, 30);
//    private static final String EVENT_DESCRIPTION_MESSAGE = "출석";
//    private static final String EVENT_DESCRIPTION_BONUS_MESSAGE = "출석 보너스";
//
//
//    //출석 이벤트 참여
//    @Transactional
//    public void participateInAttendanceEvent(UUID eventId, String userEmail) {
//        if (checkTodayAttendance(eventId, userEmail)) {
//            throw new RuntimeException("오늘 이미 출석하셨습니다.");
//        }
//
//        int monthlyCount = MonthlyAttendanceCount(eventId, userEmail) + 1;
//        long pointsToEarn = ATTENDANCE_POINT;
//        String description = EVENT_DESCRIPTION_MESSAGE;
//
//        if(monthlyCount % BONUS_PERIOD == 0){
//            pointsToEarn += BONUS_POINT;
//            description = EVENT_DESCRIPTION_BONUS_MESSAGE;
//        }
//
//        AddPointRequest addPointRequest = AddPointRequest.builder()
//                .userEmail(userEmail)
//                .eventId(eventId)
//                .pointChange(pointsToEarn)
//                .description(description)
//                .isWinner(false)
//                .participationCount(monthlyCount)
//                .build();
//
//        //프론트에서 보여주는 토탈 포인트 값과 데이터베이스 포인트 값과 일치하는지 확인합니다.
//        //checkSamePoint(userEmail,point);
//
//        //포인트 서비스에서 해당 내용을 저장, 적립은 포인트 서비스에서 구현합니다.
//        pointService.addEarnedPoint(addPointRequest);
//    }
//
//
//    //이번달 출석횟수, 누적포인트, 마지막 출석일
//    public MonthlyAttendanceResponse getAttendanceData(String userEmail, UUID eventId) {
//        User user = findUserByEmail(userEmail);
//        Event event = findEventById(eventId);
//
//        LocalDateTime startOfMonth = event.getStartDate();
//        LocalDateTime endOfMonth = event.getEndDate();
//
//        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
//                user, event, startOfMonth, endOfMonth);
//
//        int attendanceCount = points.size();
//        long totalPoints = points.stream().mapToLong(EarnedPoint::getPointChange).sum();
//        boolean isChecked = checkTodayAttendance(eventId, userEmail);
//        return new MonthlyAttendanceResponse(attendanceCount, totalPoints, isChecked, (AttendanceEvent) event);
//    }
//
//
//    public boolean checkTodayAttendance(UUID eventId, String userEmail) {
//        Event event = findEventById(eventId);
//        User user = findUserByEmail(userEmail);
//
//        LocalDateTime startOfMonth = LocalDate.now().atStartOfDay();
//        LocalDateTime endOfMonth = LocalDate.now().plusDays(1).atStartOfDay();
//
//        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(user, event, startOfMonth, endOfMonth);
//
//        return !points.isEmpty();
//    }
//
//
//    public int MonthlyAttendanceCount(UUID eventId, String userEmail) {
//        Event event = findEventById(eventId);
//        User user = findUserByEmail(userEmail);
//
//        LocalDateTime startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
//        LocalDateTime endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
//
//        List<EarnedPoint> points = pointRepository.findByUserAndEventAndCreatedAtBetween(
//                user, event, startOfMonth, endOfMonth);
//
//        return points.size();
//    }
//
//
//    private Event findEventById(UUID eventId) {
//        return eventRepository.findById(eventId)
//                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
//    }
//
//    private User findUserByEmail(String userEmail) {
//        return userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + userEmail));
//    }
//
//    private Boolean checkSamePoint(String email, long getPoint) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("유저가 없습니다."));
//
//        return user.getTotalPoint() == getPoint;
//    }
//
//
//}

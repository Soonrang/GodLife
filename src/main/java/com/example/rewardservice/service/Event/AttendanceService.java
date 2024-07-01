package com.example.rewardservice.service.Event;

import com.example.rewardservice.domain.Event.Event;
import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.domain.User.AttendanceLog;
import com.example.rewardservice.dto.Event.AttendanceDTO;
import com.example.rewardservice.repository.AttendanceLogRepository;
import com.example.rewardservice.repository.EventRepository;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceLogRepository attendanceLogRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public AttendanceDTO checkIn(String userId, String eventId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        Event event = eventRepository.findById(UUID.fromString(eventId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        LocalDateTime now = LocalDateTime.now();
        boolean alreadyCheckedIn = attendanceLogRepository.existsByUserIdAndEventIdAndCreatedAtBetween(
                userId, UUID.fromString(eventId),
                now.toLocalDate().atStartOfDay(),
                now.toLocalDate().atTime(23, 59, 59)
        );

        if (alreadyCheckedIn) {
            throw new IllegalStateException("User already checked in today");
        }

        AttendanceLog attendanceLog = AttendanceLog.builder()
                .userId(userId)
                .eventId(UUID.fromString(eventId))
                .build();

        attendanceLogRepository.save(attendanceLog);

        return new AttendanceDTO(userId, eventId, attendanceLog.getCreatedAt());
    }
}

//@Service
//@RequiredArgsConstructor
//public class AttendanceService {
//
//    private final AttendanceLogRepository attendanceLogRepository;
//    private final EventRepository eventRepository;
//    private final  UserRepository userRepository;
//    private final  PointLogRepository pointLogRepository;
//
//    public AttendanceDTO checkIn(String userId, String eventId) {
//        UUID eventUUID = UUID.fromString(eventId);
//
//        Optional<Event> eventOpt = eventRepository.findById(eventUUID);
//        if(eventOpt.isEmpty()) {
//            return AttendanceDTO.builder()
//                    .message("Event not found")
//                    .rewardPoints(0)
//                    .build();
//        }
//        Event event = eventOpt.get();
//
//        LocalDateTime now  = LocalDateTime.now();
//        if (now.isBefore(event.getStartDate()) || now.isAfter(event.getEndDate())) {
//            return AttendanceDTO.builder()
//                    .message("Event not active")
//                    .rewardPoints(0)
//                    .build();
//        }
//
//        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
//        LocalDateTime todayEnd = todayStart.plusDays(1);
//
//        boolean alreadyCheckedIn = attendanceLogRepository.existsByUserIdAndEventIdAndAttendanceDateBetween(userId, eventUUID, todayStart, todayEnd);
//        if (alreadyCheckedIn) {
//            return AttendanceDTO.builder()
//                    .message("Already checked in today")
//                    .rewardPoints(0)
//                    .build();
//    }
//        AttendanceLog log = AttendanceLog.builder()
//                .userId(userId)
//                .eventId(eventUUID)
//                .build();
//        attendanceLogRepository.save(log);
//
//        User user = userRepository.findByUserId(userId).orElseGet(()-> User.builder()
//                .id(UUID.randomUUID())
//                .index(0)
//                .userId(userId)
//                .totalPoint(0)
//                .lastUpdateDate(now)
//                .build());
//
//        long rewardPoints = 10;
//        long daysParticipated = attendanceLogRepository.countByUserIdAndEventId(userId, eventUUID);
//
//        if(daysParticipated%10==0) {
//            Random random = new Random();
//            rewardPoints = 100 + random.nextInt(901);
//    }
//        user.setTotalPoint(user.getTotalPoint() + rewardPoints);
//        user.setLastUpdateDate(now);
//        userRepository.save(user);
//
//        PointLog pointLog = PointLog.builder()
//                .user(user)
//                .transactionType("ATTENDANCE")
//                .pointChange(rewardPoints)
//                .description("Attendance reward")
//                .build();
//        pointLogRepository.save(pointLog);
//
//        return AttendanceDTO.builder()
//                .message("Checked in successfully")
//                .rewardPoints(rewardPoints)
//                .build();
//    }
//}

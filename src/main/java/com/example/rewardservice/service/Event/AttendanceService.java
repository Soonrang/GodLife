package com.example.rewardservice.service.Event;

import com.example.rewardservice.domain.Event.Event;
import com.example.rewardservice.domain.Point.Point;
import com.example.rewardservice.domain.Point.PointLog;
import com.example.rewardservice.domain.User.AttendanceLog;
import com.example.rewardservice.dto.Event.AttendanceDTO;
import com.example.rewardservice.repository.AttendanceLogRepository;
import com.example.rewardservice.repository.EventRepository;
import com.example.rewardservice.repository.PointLogRepository;
import com.example.rewardservice.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointLogRepository pointLogRepository;

    public AttendanceDTO checkIn(AttendanceDTO attendanceDTO) {
        UUID eventId = attendanceDTO.getEventId();
        String userId = attendanceDTO.getUserId();

        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if(eventOptional.isEmpty()) {
            return AttendanceDTO.builder().
                    message("Event not found").rewardPoints(0).build();
        }
        Event event = eventOptional.get();

        LocalDateTime now  = LocalDateTime.now();
        if (now.isBefore(event.getStartDate() || now.isAfter(event.getEndDate())) {
            return AttendanceDTO.builder()
                    .message("Event not active")
                    .rewardPoints(0)
                    .build();
        }

        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        boolean alreadyCheckedIn = attendanceLogRepository.existsByUserIdAndEventIdAndAttendanceDateBetween(userId, eventId, todayStart, todayEnd);
        if (alreadyCheckedIn) {
            return "Already checked in today";

    }
        AttendanceLog log = AttendanceLog.builder()
                .userId(userId)
                .eventId(eventId)
                .build();
        attendanceLogRepository.save(log);

        Point point =pointRepository.findByUserId(userId).orElseGet(()-> Point.builder()
                .id(UUID.randomUUID())
                .index(0)
                .userId(userId)
                .totalPoint(0)
                .lastUpdateDate(now)
                .build());

        long rewardPoints = 10;
        long daysParticipated = attendanceLogRepository.countByUserIdAndEventId(userId, eventId);

        if(daysParticipated%10==0) {
            Random random = new Random();
            rewardPoints = 100 + random.nextInt(901);
    }
        point.setTotalPoint(point.getTotalPoint() + rewardPoints);
        point.setLastUpdateDate(now);
        pointRepository.save(point);

        PointLog pointLog = PointLog.builder()
                .point(point)
                .transactionType("ATTENDANCE")
                .pointChange(rewardPoints)
                .description("Attendance reward")
                .createdAt(now)
                .build();
        pointLogRepository.save(pointLog);

        return AttendanceDTO.builder()
                .message("Checked in successfully")
                .rewardPoints(rewardPoints)
                .build();
    }
    }
}

package com.example.rewardservice.Event.application;

import com.example.rewardservice.Event.application.repository.EventRepository;
import com.example.rewardservice.Event.domain.Event;
import com.example.rewardservice.Event.domain.EventType.AttendanceEvent;
import com.example.rewardservice.Participation.ParticipationRepository;
import com.example.rewardservice.Participation.domain.Participation;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

//    @Transactional
//    public void participateInAttendanceEvent(UUID eventId, String userEmail) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(()-> new RuntimeException("이벤트 없음"));
//
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(()-> new RuntimeException("유저 없음"));
//
//        List<Participation> participations =
//                participationRepository.findByUserAndEventAndParticipationDateBetween(
//                        user,
//                        event,
//                        LocalDateTime.now().withDayOfMonth(1),
//                        LocalDateTime.now().plusMonths(1).withDayOfMonth(1).minusSeconds(1)
//                );
//
//        int participationCount = participations.size() + 1;
//
//        Participation participation = Participation.builder()
//                .event(event)
//                .user(user)
//                .monthlyParticipationCount(participationCount)
//                .isWinner(false)
//                .rewardAmount(0)
//                .build();
//
//        participationRepository.save(participation);
//
//        if(participationCount % 10 == 0) {
//            long bonusPoints = ((AttendanceEvent) event).getBonusPoints();
//            user.setTotalPoint(user.getTotalPoint() + bonusPoints);
//            userRepository.save(user);
//        }
//    }



}

package com.example.rewardservice.common.dataInsert;

import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventPeriod;
import com.example.rewardservice.event.domain.EventType;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.user.domain.MemberState;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ServiceInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public void createAutoUser() {
        String email = "admin@gmail.com";

        // 이메일로 사용자가 존재하는지 확인
        boolean userExists = userRepository.existsByEmail(email);

        if (!userExists) {
            String encodedPassword = passwordEncoder.encode("111111111");

            User user = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .name("admin")
                    .nickname("admin_nickname")
                    .totalPoint(0)
                    .profileImage("default.jpg")
                    .memberState(MemberState.ACTIVE)
                    .userSocial(false)
                    .build();

            userRepository.save(user);
        }
    }

    @Transactional
    public void createAutoEvents() {
        String attendanceEventName = "7월 출석 이벤트";
        String rouletteEventName = "7월 룰렛 이벤트";
        String pageEventName = "페이지 이동 이벤트";

        // 이벤트가 존재하는지 확인
        boolean attendanceEventExists = eventRepository.existsByName(attendanceEventName);
        boolean rouletteEventExists = eventRepository.existsByName(rouletteEventName);
        boolean pageEventExists = eventRepository.existsByName(pageEventName);

        if (!attendanceEventExists) {
            Event attendanceEvent = Event.builder()
                    .name(attendanceEventName)
                    .eventState("ACTIVE")
                    .eventType(EventType.ATTENDANCE)
                    .eventPeriod(new EventPeriod(
                            LocalDateTime.of(2024, 7, 1, 0, 0),
                            LocalDateTime.of(2024, 7, 31, 23, 59)
                    ))
                    .build();

            eventRepository.save(attendanceEvent);
        }

        if (!attendanceEventExists) {
            Event attendanceEvent = Event.builder()
                    .name( "8월 출석 이벤트")
                    .eventState("ACTIVE")
                    .eventType(EventType.ATTENDANCE)
                    .eventPeriod(new EventPeriod(
                            LocalDateTime.of(2024, 8, 1, 0, 0),
                            LocalDateTime.of(2024, 7, 31, 23, 59)
                    ))
                    .build();

            eventRepository.save(attendanceEvent);
        }

        if (!rouletteEventExists) {
            Event rouletteEvent = Event.builder()
                    .name(rouletteEventName)
                    .eventState("ACTIVE")
                    .eventType(EventType.ROULETTE)
                    .eventPeriod(new EventPeriod(
                            LocalDateTime.of(2024, 7, 1, 0, 0),
                            LocalDateTime.of(2024, 7, 31, 23, 59)
                    ))
                    .build();

            eventRepository.save(rouletteEvent);
        }

        if (!rouletteEventExists) {
            Event rouletteEvent = Event.builder()
                    .name( "8월 룰렛 이벤트")
                    .eventState("ACTIVE")
                    .eventType(EventType.ROULETTE)
                    .eventPeriod(new EventPeriod(
                            LocalDateTime.of(2024, 8, 1, 0, 0),
                            LocalDateTime.of(2024, 8, 31, 23, 59)
                    ))
                    .build();

            eventRepository.save(rouletteEvent);
        }

        if(!pageEventExists) {
            Event pageEvent = Event.builder()
                    .name(pageEventName)
                    .eventState("ACTIVE")
                    .eventType(EventType.VIEW)
                    .eventPeriod(new EventPeriod(
                            LocalDateTime.of(2024, 7, 1, 0, 0),
                            LocalDateTime.of(2024, 10, 30, 23, 59)
                    ))
                    .build();
            eventRepository.save(pageEvent);
        }
    }
}
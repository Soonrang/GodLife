package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.EventRegisterRequest;
import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.EventType.AttendanceEvent;
import com.example.rewardservice.event.domain.EventType.RouletteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

    // 이벤트 등록 메서드 추가
    public Event registerEvent(EventRegisterRequest eventRegisterRequest) {
        Event event;
        if ("ATTENDANCE".equalsIgnoreCase(eventRegisterRequest.getEventType())) {
            event = AttendanceEvent.builder()
                    .name(eventRegisterRequest.getName())
                    .description(eventRegisterRequest.getDescription())
                    .startDate(eventRegisterRequest.getStartDate())
                    .endDate(eventRegisterRequest.getEndDate())
                    .eventState(eventRegisterRequest.getEventState())
                    .eventType(eventRegisterRequest.getEventType())
                    .build();
        } else if ("ROULETTE".equalsIgnoreCase(eventRegisterRequest.getEventType())) {
            event = RouletteEvent.builder()
                    .name(eventRegisterRequest.getName())
                    .description(eventRegisterRequest.getDescription())
                    .startDate(eventRegisterRequest.getStartDate())
                    .endDate(eventRegisterRequest.getEndDate())
                    .eventState(eventRegisterRequest.getEventState())
                    .eventType(eventRegisterRequest.getEventType())
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported event type: " + eventRegisterRequest.getEventType());
        }

        return eventRepository.save(event);
    }


}

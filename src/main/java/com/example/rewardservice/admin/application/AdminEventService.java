package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.EventRegisterRequest;
import com.example.rewardservice.admin.application.dto.EventResponse;
import com.example.rewardservice.admin.application.dto.EventUpdateRequest;
import com.example.rewardservice.event.domain.EventPeriod;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

    @Transactional
    public EventResponse createEvent(EventRegisterRequest request) {
        EventPeriod eventPeriod = new EventPeriod(request.getStartDate(), request.getEndDate());
        Event event = new Event(request.getName(), request.getEventState(), request.getEventType(), eventPeriod);
        Event createdEvent = eventRepository.save(event);
        return new EventResponse(createdEvent.getId(), createdEvent.getName(), createdEvent.getEventState(), createdEvent.getEventType(), createdEvent.getEventPeriod());
    }

    @Transactional
    public EventResponse updateEvent(UUID eventId, EventUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));
        EventPeriod eventPeriod = new EventPeriod(request.getStartDate(), request.getEndDate());
        event.updateEvent(request.getName(), request.getEventState(), request.getEventType(), eventPeriod);
        Event updatedEvent = eventRepository.save(event);
        return new EventResponse(updatedEvent.getId(), updatedEvent.getName(), updatedEvent.getEventState(), updatedEvent.getEventType(), updatedEvent.getEventPeriod());
    }

    //이미 등록된 이벤트인지 확인하는 메서드
    //이벤트 종료시점이 시작시점 이후인지 확인하는 메서드


}

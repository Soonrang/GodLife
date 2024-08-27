package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.request.EventRegisterRequest;
import com.example.rewardservice.admin.application.dto.response.EventResponse;
import com.example.rewardservice.admin.application.dto.request.EventUpdateRequest;
import com.example.rewardservice.event.domain.EventPeriod;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.image.s3.S3ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final S3ImageService s3ImageService;

    @Transactional
    public EventResponse createEvent(EventRegisterRequest request) {
        EventPeriod eventPeriod = new EventPeriod(request.getStartDate(), request.getEndDate());
        String mainUrl = null;
        String bannerUrl = null;

        if(request.getImageMain()!= null) {
            mainUrl = s3ImageService.upload(request.getImageMain());
        }

        if(request.getImageBanner()!= null) {
            bannerUrl = s3ImageService.upload(request.getImageBanner());
        }

        Event event = Event.builder()
                .name(request.getName())
                .eventState(request.getEventState())
                .eventType(request.getEventType())
                .eventPeriod(eventPeriod)
                .eventImageMain(mainUrl)
                .eventImageBanner(bannerUrl)
                .build();

        Event createdEvent = eventRepository.save(event);
        return new EventResponse(createdEvent.getId(), createdEvent.getName(), createdEvent.getEventState(), createdEvent.getEventType(), createdEvent.getEventPeriod(), createdEvent.getEventImageMain(), createdEvent.getEventImageBanner());
    }

    @Transactional
    public EventResponse updateEvent(UUID eventId, EventUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));
        EventPeriod eventPeriod = new EventPeriod(request.getStartDate(), request.getEndDate());

        String mainUrl = event.getEventImageMain();
        String bannerUrl = event.getEventImageBanner();

        if(request.getImageMain() != null) {
            mainUrl = s3ImageService.upload(request.getImageMain());
        }

        if(request.getImageBanner() != null) {
            bannerUrl = s3ImageService.upload(request.getImageBanner());
        }

        event.updateEvent(request.getName(), request.getEventState(), request.getEventType(), eventPeriod, mainUrl, bannerUrl);
        Event updatedEvent = eventRepository.save(event);
        return new EventResponse(updatedEvent.getId(), updatedEvent.getName(), updatedEvent.getEventState(), updatedEvent.getEventType(), updatedEvent.getEventPeriod(), mainUrl, bannerUrl);
    }

    public List<EventResponse> viewAllEvent() {
        List<Event> eventList = eventRepository.findAll();

        return eventList.stream()
                .map( event -> new EventResponse(event.getId(),
                        event.getName(),
                        event.getEventState(),
                        event.getEventType(),
                        event.getEventPeriod(),
                        event.getEventImageBanner(),   // 추가된 이미지 필드
                        event.getEventImageMain()))
                        .collect(Collectors.toList());
    }

    public EventResponse changeEventStatus(UUID eventId, String eventState){
        Event event = eventRepository.getById(eventId);
        event.changeEventStatus(eventState);
        return new EventResponse(eventId,event.getName(),eventState,event.getEventType(),event.getEventPeriod(),event.getEventImageMain(),event.getEventImageBanner());
    }


    //이미 등록된 이벤트인지 확인하는 메서드
    //이벤트 종료시점이 시작시점 이후인지 확인하는 메서드


}

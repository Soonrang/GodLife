package com.example.rewardservice.service;

import com.example.rewardservice.domain.Event.Event;
import com.example.rewardservice.dto.Event.EventDTO;
import com.example.rewardservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = dtoToEntity(eventDTO);
        eventRepository.save(event);
        return entityToDto(event);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public EventDTO getEventById(UUID id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(this::entityToDto).orElse(null);
    }

    public EventDTO updateEvent(UUID id, EventDTO eventDTO) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.updateEvent(
                            eventDTO.getName(),
                            eventDTO.getEventType(),
                            eventDTO.getDescription(),
                            eventDTO.getStartDate(),
                            eventDTO.getEndDate(),
                            eventDTO.getEventState(),
                            eventDTO.getReward()
                    );
                    eventRepository.save(event);
                    return entityToDto(event);
                })
                .orElse(null);

    }

    public boolean deleteEvent(UUID id) {
       if(eventRepository.existsById(id)) {
           eventRepository.deleteById(id);
           return true;
       }
        return false;
    }

    private Event dtoToEntity(EventDTO eventDTO) {
        return Event.builder()
                .name(eventDTO.getName())
                .eventType(eventDTO.getEventType())
                .description(eventDTO.getDescription())
                .startDate(eventDTO.getStartDate())
                .endDate(eventDTO.getEndDate())
                .announcementDate(eventDTO.getAnnouncementDate())
                .eventState(eventDTO.getEventState())
                .reward(eventDTO.getReward())
                .build();
    }

    private EventDTO entityToDto(Event event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .eventType(event.getEventType())
                .description(event.getDescription())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .announcementDate(event.getAnnouncementDate())
                .eventState(event.getEventState())
                .reward(event.getReward())
                .build();
    }
}

package com.example.rewardservice.controller;

import com.example.rewardservice.dto.Event.EventDTO;
import com.example.rewardservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public EventDTO createEvent(@RequestParam String name,
                                @RequestParam String eventType,
                                @RequestParam String description,
                                @RequestParam LocalDateTime startDate,
                                @RequestParam LocalDateTime endDate,
                                @RequestParam LocalDateTime announcementDate,
                                @RequestParam String eventState,
                                @RequestParam int reward) {
        EventDTO eventDTO = EventDTO.builder()
                .name(name)
                .eventType(eventType)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .announcementDate(announcementDate)
                .eventState(eventState)
                .reward(reward)
                .build();
        return eventService.createEvent(eventDTO);
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable UUID id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable UUID id,
                                @RequestParam String name,
                                @RequestParam String eventType,
                                @RequestParam String description,
                                @RequestParam LocalDateTime startDate,
                                @RequestParam LocalDateTime endDate,
                                @RequestParam LocalDateTime announcementDate,
                                @RequestParam String eventState,
                                @RequestParam int reward) {
        EventDTO eventDTO = EventDTO.builder()
                .name(name)
                .eventType(eventType)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .announcementDate(announcementDate)
                .eventState(eventState)
                .reward(reward)
                .build();
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
    }
}
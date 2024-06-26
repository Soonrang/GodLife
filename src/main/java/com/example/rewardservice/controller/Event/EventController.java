package com.example.rewardservice.controller.Event;

import com.example.rewardservice.dto.Event.EventDTO;
import com.example.rewardservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;


    /* @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    } */

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestParam String name,
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
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return ResponseEntity.ok(createdEvent);
    }


    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }



    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable UUID id) {
        EventDTO event = eventService.getEventById(id);
        if(event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable UUID id,
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
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
        if(updatedEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        boolean deleted = eventService.deleteEvent(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
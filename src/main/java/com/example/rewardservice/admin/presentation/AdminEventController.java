package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminEventService;
import com.example.rewardservice.admin.application.dto.request.EventRegisterRequest;
import com.example.rewardservice.admin.application.dto.response.EventResponse;
import com.example.rewardservice.admin.application.dto.request.EventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/event")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    // 출석 이벤트 등록
    @PostMapping("/register")
    public ResponseEntity<EventResponse> registerEvent(@RequestBody EventRegisterRequest eventRegisterRequest) {
        EventResponse createdEvent = adminEventService.createEvent(eventRegisterRequest);
        return ResponseEntity.ok(createdEvent);
    }

    // 출석 이벤트 업데이트
    @PutMapping("/update/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable UUID id, @RequestBody EventUpdateRequest eventUpdateRequest) {
        EventResponse updatedEvent = adminEventService.updateEvent(id, eventUpdateRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<EventResponse>> viewAllEvents(){
        return ResponseEntity.ok(adminEventService.viewAllEvent());
    }

    @PostMapping("/change-event-stauts")
    public ResponseEntity<EventResponse> updateStatusEvent(@PathVariable UUID eventId, @RequestParam("eventState") String eventState){
        return ResponseEntity.ok(adminEventService.changeEventStatus(eventId, eventState));
    }
}


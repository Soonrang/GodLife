package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminEventService;
import com.example.rewardservice.admin.application.dto.EventRegisterRequest;
import com.example.rewardservice.admin.application.dto.EventResponse;
import com.example.rewardservice.admin.application.dto.EventUpdateRequest;
import com.example.rewardservice.event.domain.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/event")
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
}


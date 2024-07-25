package com.example.rewardservice.admin.presentation;

import com.example.rewardservice.admin.application.AdminEventService;
import com.example.rewardservice.admin.application.dto.EventRegisterRequest;
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
    public ResponseEntity<Event> registerEvent(@RequestBody EventRegisterRequest eventRegisterRequest) {
        Event createdEvent = adminEventService.registerEvent(eventRegisterRequest);
        return ResponseEntity.ok(createdEvent);
    }

}


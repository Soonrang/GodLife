package com.example.rewardservice.controller;

import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/participate")
    public ResponseEntity<PointLogDTO> participateInEvent(@RequestParam String userId, @RequestParam UUID eventId, @RequestParam String answer) {
        try {
            PointLogDTO pointLogDTO = eventService.participateInEvent(userId, eventId, answer);
            return ResponseEntity.ok(pointLogDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PointLogDTO(null, null, "Error", 0, e.getMessage(), null));
        }
    }
}

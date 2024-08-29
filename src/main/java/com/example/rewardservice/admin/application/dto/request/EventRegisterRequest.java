package com.example.rewardservice.admin.application.dto.request;

import com.example.rewardservice.event.domain.EventType;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class EventRegisterRequest {
    private String name;
    private String eventState;
    private EventType eventType;
    private LocalDate startDate;
    private LocalDate endDate;
    private MultipartFile imageMain;
    private MultipartFile imageBanner;
}

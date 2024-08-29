package com.example.rewardservice.admin.application.dto.response;

import com.example.rewardservice.event.domain.EventPeriod;
import com.example.rewardservice.event.domain.EventType;
import com.example.rewardservice.shop.application.response.ProductImageResponse;
import com.example.rewardservice.shop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private String name;
    private String eventState;
    private EventType eventType;
    private EventPeriod eventPeriod;
    private String imageMain;
    private String imageBanner;
}

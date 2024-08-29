package com.example.rewardservice.admin.application.dto.request;

import com.example.rewardservice.event.domain.EventPeriod;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
public class DonationRegisterRequest {

    private String title;
    private long currentAmount;
    private long targetAmount;
    private LocalDate startDate;
    private LocalDate  endDate;
    private MultipartFile imageUrl;

    public EventPeriod getEventPeriod() {
        return new EventPeriod(startDate, endDate);
    }
}
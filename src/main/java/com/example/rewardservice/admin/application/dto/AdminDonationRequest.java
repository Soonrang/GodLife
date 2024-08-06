package com.example.rewardservice.admin.application.dto;

import com.example.rewardservice.event.domain.EventPeriod;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public record AdminDonationRequest(String title, long currentAmount, long targetAmount, EventPeriod eventPeriod,
                                   MultipartFile imageUrl) {


}

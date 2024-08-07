package com.example.rewardservice.admin.application.dto;

import com.example.rewardservice.event.domain.EventPeriod;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record AdminDonationRequest(String title, long currentAmount, long targetAmount, EventPeriod eventPeriod,
                                   @Nullable MultipartFile imageUrl) {


}
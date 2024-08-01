package com.example.rewardservice.user.application.response;

import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.user.domain.GiftRecord;
import com.example.rewardservice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class PointRecordResponse {

    private String type;
    private String description;
    private long points;
    private LocalDateTime createdAt;

    public static PointRecordResponse from(Point point) {
        return PointRecordResponse.builder()
                .type(point.getType())
                .description(point.getDescription())
                .points(point.getAmount())
                .createdAt(point.getCreatedAt())
                .build();
    }
}

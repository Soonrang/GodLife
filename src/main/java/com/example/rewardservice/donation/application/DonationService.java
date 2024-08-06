package com.example.rewardservice.donation.application;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.donation.application.dto.DonatePointRequest;
import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRecordRepository donationRecordRepository;
    private final ValidateService validateService;
    private final PointService pointService;

    @Transactional
    public void donatePoints(DonatePointRequest donationRequest,UUID id) {
        User user = validateService.findByUserEmail(donationRequest.getUserEmail());
        Donation donation = validateService.findByDonationId(id);

        // 기부 기록 저장
        DonationRecord donationRecord = new DonationRecord(user, donation, donationRequest.getPoints());
        donationRecordRepository.save(donationRecord);

        // 포인트 기부 기록 저장
        DonatePointRequest donatePointRequest = DonatePointRequest.builder()
                .userEmail(donationRequest.getUserEmail())
                .points(donationRequest.getPoints())
                .description("기부: " + donation.getTitle())
                .activityId(donationRecord.getId())
                .build();
        pointService.donatePoints(donatePointRequest);
    }

}

package com.example.rewardservice.donation.application;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.donation.application.dto.DonatePointRequest;
import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final DonationRecordRepository donationRecordRepository;
    private final UserRepository userRepository;
    private final PointService pointService;

    @Transactional
    public void donatePoints(UUID donationId, String email, long points) {
        User user = findByUserEmail(email);
        Donation donation = findByDonationId(donationId);

        // 기부 기록 저장
        DonationRecord donationRecord = new DonationRecord(user, donation, points);
        donationRecordRepository.save(donationRecord);

        // 기부 총액 변동
        donation.addDonation(points);
        donationRepository.save(donation);

        // 포인트 기부 기록 저장
        DonatePointRequest donatePointRequest = DonatePointRequest.builder()
                .userEmail(email)
                .points(points)
                .description("기부: " + donation.getTitle())
                .activityId(donationRecord.getId())
                .build();
        pointService.donatePoints(donatePointRequest);
    }

    public Donation findByDonationId(UUID donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 기부 정보가 없습니다: " + donationId));
    }

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

}

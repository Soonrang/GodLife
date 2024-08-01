package com.example.rewardservice.donation.application;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;
    private final DonationRecordRepository donationRecordRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final ValidateService validateService;

    //read

}

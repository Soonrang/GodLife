package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ChallengeStatusService {

    private final ChallengeRepository challengeRepository;

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0 00 0 * * *")
    @Transactional
    public void updateChallengeStatus() {
        LocalDate today = LocalDate.now();

        List<Challenge> upcomingChallenges = challengeRepository.findChallengesForUpcoming(today);
        for (Challenge challenge : upcomingChallenges) {
            challenge.updateStatus("진행전");
        }

        List<Challenge> ongoingChallenges = challengeRepository.findChallengesForOngoing(today);
        for (Challenge challenge : ongoingChallenges) {
            challenge.updateStatus("진행중");
        }

        List<Challenge> challengesToExpire = challengeRepository.findChallengesToExpire(today);

        // 상태를 '종료'로 변경
        for (Challenge challenge : challengesToExpire) {
            challenge.updateStatus("종료");
            challengeRepository.save(challenge);  // 상태 변경 후 저장
        }

        challengeRepository.saveAll(ongoingChallenges);
        challengeRepository.saveAll(upcomingChallenges);

    }

}

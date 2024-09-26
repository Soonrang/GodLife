package com.example.rewardservice.challenge.application.service;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.repsoitory.ChallengeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.rewardservice.challenge.domain.vo.ChallengeStates.END_STATE;

@Service
@AllArgsConstructor
public class ChallengeStatusService {

    private final ChallengeRepository challengeRepository;

    private final static String UPCOMING_STATE = "진행전";
    private final static String ONGOING_STATE = "진행중";
    private final static String EDB_STATE = "종료";



    @PostConstruct
    public void init() {
        updateChallengeStatus();  // 애플리케이션 시작 시 한번 실행
    }

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0 00 0 * * *")
    @Transactional
    public void updateChallengeStatus() {
        LocalDate today = LocalDate.now();

        List<Challenge> upcomingChallenges = challengeRepository.findChallengesForUpcoming(today);
        for (Challenge challenge : upcomingChallenges) {
            challenge.updateStatus(UPCOMING_STATE);
        }

        List<Challenge> ongoingChallenges = challengeRepository.findChallengesForOngoing(today);
        for (Challenge challenge : ongoingChallenges) {
            challenge.updateStatus(ONGOING_STATE);
        }

        List<Challenge> challengesToExpire = challengeRepository.findChallengesToExpire(today);

        // 상태를 '종료'로 변경
        for (Challenge challenge : challengesToExpire) {
            challenge.updateStatus(END_STATE);
            challengeRepository.save(challenge);  // 상태 변경 후 저장
        }

        challengeRepository.saveAll(ongoingChallenges);
        challengeRepository.saveAll(upcomingChallenges);

    }

}

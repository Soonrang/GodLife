package com.example.rewardservice.Event.application;

import com.example.rewardservice.Event.application.repository.RouletteRepository;
import com.example.rewardservice.Participation.ParticipationRepository;
import com.example.rewardservice.Participation.domain.Participation;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouletteService {

    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    public boolean hasParticipatedToday(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("아이디없음"));
        LocalDate today = LocalDate.now();
        List<Participation> participations = participationRepository.findByUserAndParticipationDateBetween(
                user, today.atStartOfDay(), today.plusDays(1).atStartOfDay()
        );
        return !participations.isEmpty();
    }

    public void updatePoints(String userEmail, long earnedPoints){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("아이디없음"));
        user.earnPoints(earnedPoints);
        userRepository.save(user);

        Participation participation = new Participation();
        participation.setUser(user);
        participation.setParticipationDate(LocalDateTime.now());
        participationRepository.save(participation);
    }
}

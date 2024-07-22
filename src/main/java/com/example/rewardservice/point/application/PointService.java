package com.example.rewardservice.point.application;

import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.point.domain.EarnedPoint;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void addEarnedPoints(User user, long points, String description, Event event,
                                int participationCount, boolean isWinner, String rewardType){
        user.earnPoints(points);
        userRepository.save(user);

        EarnedPoint earnedPoint = EarnedPoint.builder()
                .user(user)
                .pointChange(points)
                .description(description)
                .event(event)
                .participationCount(participationCount)
                .isWinner(isWinner)
                .rewardType(rewardType)
                .build();

        pointRepository.save(earnedPoint);
    }

}

package com.example.rewardservice.point.application;

import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.application.dto.UsePointRequest;
import com.example.rewardservice.point.domain.EarnedPoint;
import com.example.rewardservice.point.domain.UsedPoint;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    private static final String POINT_TYPE_EARNED = "적립";
    private static final String POINT_TYPE_USED = "사용";


    @Transactional
    public void addEarnedPoint(AddPointRequest addPointRequest) {
        User user = findUserByEmail(addPointRequest.getUserEmail());
        Event event = findEventById(addPointRequest.getEventId());

        addPoint(event, user, addPointRequest);
    }


    private void addPoint(Event event, User user, AddPointRequest addPointRequest){

        EarnedPoint earnedPoint = EarnedPoint.builder()
                .event(event)
                .user(user)
                .pointChange(addPointRequest.getPointChange())
                .description(addPointRequest.getDescription())
                .isWinner(addPointRequest.isWinner())
                .rewardType(addPointRequest.getRewardType())
                .pointType(POINT_TYPE_EARNED)
                .participationCount(addPointRequest.getParticipationCount())
                .build();

        pointRepository.save(earnedPoint);
        user.earnPoints(addPointRequest.getPointChange());
        userRepository.save(user);
    }

    @Transactional
    public void usedPoints(UsePointRequest usePointRequest) {
        User user = findUserByEmail(usePointRequest.getUserEmail());
        Product product = findProductById(usePointRequest.getProductId());

        UsedPoint usedPoint = new UsedPoint(
                product,
                user,
                usePointRequest.getPointChange(),
                usePointRequest.getDescription(),
                POINT_TYPE_USED
        );

        pointRepository.save(usedPoint);
        user.usePoints(usePointRequest.getPointChange());
        userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Event findEventById(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private Product findProductById(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 없습니다: " + productId));
    }

}

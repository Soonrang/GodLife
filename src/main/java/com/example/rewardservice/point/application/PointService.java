package com.example.rewardservice.point.application;

import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.application.dto.GiftPointRequest;
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
    private static final String POINT_TYPE_GIFT = "선물";



    @Transactional
    public void addEarnedPoint(AddPointRequest addPointRequest) {
        User user = findByUserEmail(addPointRequest.getUserEmail());
        Event event = findByEventId(addPointRequest.getEventId());

        addPointsByEvent(event, user, addPointRequest);
    }

    @Transactional
    public void usedPoints(UsePointRequest usePointRequest) {
        User user = findByUserEmail(usePointRequest.getUserEmail());
        Product product = findByProductId(usePointRequest.getProductId());

        UsedPoint usedPoint = new UsedPoint(
                product,
                user,
                usePointRequest.getPointChange(),
                usePointRequest.getDescription(),
                POINT_TYPE_USED
        );

        pointRepository.save(usedPoint);
        user.validateUsePoints(usePointRequest.getPointChange());
        userRepository.save(user);
    }

    @Transactional
    public void giftPoints(User sender, GiftPointRequest giftPointRequest) {
        User receiver = findByUserEmail(giftPointRequest.getReceiverEmail());

        if(sender.getTotalPoint() < giftPointRequest.getPointAmount()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        deductPointsByGift(sender, giftPointRequest.getPointAmount());
        addPointsByGift(receiver, giftPointRequest.getPointAmount());
        recordPointTransaction(sender, receiver, giftPointRequest);
    }

    private void addPointsByEvent(Event event, User user, AddPointRequest addPointRequest){

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


    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Event findByEventId(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private Product findByProductId(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 없습니다: " + productId));
    }


    // 포인트 선물하기
    private void deductPointsByGift(User user, long points) {
        user.validateUsePoints(points);
        userRepository.save(user);
    }

    private void addPointsByGift(User user, long points) {
        user.earnPoints(points);
        userRepository.save(user);
    }

    // 포인트 선물하기 기록
    private void recordPointTransaction(User sender, User receiver, GiftPointRequest giftPointRequest) {
        EarnedPoint senderPointRecord = EarnedPoint.builder()
                .user(sender)
                .pointChange(-giftPointRequest.getPointAmount())
                .description("Gift to " + giftPointRequest.getReceiverEmail()+ ": " + giftPointRequest.getDescription())
                .pointType(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(senderPointRecord);


        EarnedPoint recipientPointRecord = EarnedPoint.builder()
                .user(receiver)
                .pointChange(giftPointRequest.getPointAmount())
                .description("Gift from " + giftPointRequest.getSenderEmail() + ": " + giftPointRequest.getDescription())
                .pointType(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(recipientPointRecord);
    }

}

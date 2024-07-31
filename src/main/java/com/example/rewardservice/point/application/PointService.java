package com.example.rewardservice.point.application;

import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.application.dto.ViewPointRequest;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.application.dto.GiftPointRequest;
import com.example.rewardservice.point.application.dto.UsePointRequest;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final EventParticipationRepository eventParticipationRepository;

    private static final String POINT_TYPE_EARNED = "적립";
    private static final String POINT_TYPE_USED = "사용";
    private static final String POINT_TYPE_GIFT = "선물";
    private static final String POINT_TYPE_VIEW = "뷰어";


    @Transactional
    public void addEarnedPoint(AddPointRequest addPointRequest) {
        User user = findByUserEmail(addPointRequest.getUserEmail());
        Event event = findByEventId(addPointRequest.getEventId());

        Point point = Point.builder()
                .event(event)
                .user(user)
                .amount(addPointRequest.getPoint())
                .description(addPointRequest.getDescription())
                .type(POINT_TYPE_EARNED)
                .build();

        pointRepository.save(point);
        user.earnPoints(addPointRequest.getPoint());
        userRepository.save(user);
    }

    @Transactional
    public void usedPoints(UsePointRequest usePointRequest) {
        User user = findByUserEmail(usePointRequest.getUserEmail());
        Product product = findByProductId(usePointRequest.getProductId());

        Point usedPoint = new Point(
                product,
                user,
                usePointRequest.getPoint(),
                usePointRequest.getDescription(),
                POINT_TYPE_USED
        );

        pointRepository.save(usedPoint);
        user.validateUsePoints(usePointRequest.getPoint());
        userRepository.save(user);
    }

    @Transactional
    public void giftPoints(String email, GiftPointRequest giftPointRequest) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User sender = userOptional.orElseThrow(() -> new RuntimeException("아이디 없음"));

        User receiver = findByUserEmail(giftPointRequest.getRecipientId());

        if(sender.getTotalPoint() < giftPointRequest.getPoints()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        deductPointsByGift(sender, giftPointRequest.getPoints());
        addPointsByGift(receiver, giftPointRequest.getPoints());
        recordPointTransaction(sender, receiver, giftPointRequest);
    }

    @Transactional
    public void viewPoints(String email, ViewPointRequest viewPointRequest) {
        User user = findByUserEmail(email);

        Point viewPoint = Point.builder()
                .user(user)
                .type(POINT_TYPE_VIEW)
                .amount(viewPointRequest.getPoints())
                .description(viewPointRequest.getPageName())
                .build();

        user.earnPoints(viewPointRequest.getPoints());

        pointRepository.save(viewPoint);
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
        Point senderPointRecord = Point.builder()
                .user(sender)
                .amount(-giftPointRequest.getPoints())
                .description(giftPointRequest.getRecipientId() + "에게 포인트를 선물하였습니다.")
                .type(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(senderPointRecord);


        Point recipientPointRecord = Point.builder()
                .user(receiver)
                .amount(giftPointRequest.getPoints())
                .description(giftPointRequest.getSenderId() + "이 보낸 포인트 선물입니다.")
                .type(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(recipientPointRecord);
    }



}
